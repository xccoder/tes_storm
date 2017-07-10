package com.edcs.tds.storm.topology.calc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.engine.groovy.exception.GroovyException;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.service.CacheService;

import groovy.lang.Binding;
import groovy.lang.Script;
import redis.clients.jedis.Jedis;

public class RuleCalc {

    private static final Logger logger = LoggerFactory.getLogger(RuleCalc.class);

    public void TestingRuleCalc(ScriptExecutor scriptExecutor, ExecuteContext executeContext, Binding shellContext,
                                ConcurrentMap<String, List<RuleConfig>> ruleConfig, CacheService cacheService) {

        TestingMessage testingMessage = executeContext.getTestingMessage();//实时数据
        List<MDprocessInfo> mDprocessInfos = JsonUtils.toArray(cacheService.getProcessInfoJson(), MDprocessInfo.class);//主数据
        MDprocessInfo mDprocessInfo = null;
        for (MDprocessInfo mDprocessInfo2 : mDprocessInfos) {
            if (testingMessage.getRemark().equals(mDprocessInfo2.getRemark())) {
                //获取当前测试的流程
                mDprocessInfo = mDprocessInfo2;
            }
        }
        Jedis jedis = cacheService.getProxyJedisPool().getResource();
        List<TestingResultData> listResult = new ArrayList<TestingResultData>();//用来存放结果数据
        //int matchedCount = 0;
        String alterLevel = null;
        String key = null;
        int sequenceNumber = 0;//同一个工步中报警的序号
        //遍历每一个场景
        Set<String> keys = ruleConfig.keySet();//获取所有的key 的值
        for (String string : keys) {

        	sequenceNumber++;
			List<RuleConfig> rule = ruleConfig.get(string);//获取每一个场景的值
			String[] strs = string.split("_");
			String sceneName = strs[1];//场景名称
			for (RuleConfig ruleConfig2 : rule) {//遍历一个场景下的所有规则
				if(testingMessage.getStepName().equals(ruleConfig2.getStepName())){
					long executeUsedTime = 0;
	                long executeBeginTime = System.currentTimeMillis();
					Script script = CacheService.getScriptCache().get(ruleConfig2.getStepSign()).getRight();
					script.setBinding(shellContext);
                    try {
                        // 返回值为 ： 报警上限_报警下限_比较值_报警级别
                        alterLevel = scriptExecutor.execute(script);
                    } catch (GroovyException e) {
                        executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                        logger.error("Rule execute error, rule id: {}." + e, ruleConfig2.getId());
                        executeContext.addRuleException(ruleConfig2, e, executeUsedTime);
                    } finally {
                        executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                    }
                    if (StringUtils.isNotBlank(alterLevel) && !alterLevel.equals("null")) {
                        //matchedCount++;
                        // TODO 记录匹配的规则和相关数据
                        executeContext.addMatchedRule(ruleConfig2.getId(), 0d, executeUsedTime);
                        // TODO 将结果集写入Redis缓存
                        String[] alters = alterLevel.split("_");
                        BigDecimal upLimit = BigDecimal.valueOf(Long.valueOf(alters[1]));//报警上限
                        BigDecimal lowLimit = BigDecimal.valueOf(Long.valueOf(alters[2]));//报警下限
                        int alterLe = Integer.parseInt(alters[4]);//报警级别
                        // key = 流程号+序号
                        key = mDprocessInfo.getRemark() + testingMessage.getSequenceId();
                        String handle = "TxAlertInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + testingMessage.getSfc() + "," + sceneName;
                        TestingResultData testingResultData = new TestingResultData();
                        testingResultData.setHandle(handle);
                        testingResultData.setSite(mDprocessInfo.getSite());
                        testingResultData.setCategory(sceneName);
                        testingResultData.setAltetSequenceNumber(sequenceNumber);
                        //TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>
                        testingResultData.setTxAlertListInfoBO("TxAlertInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + sceneName);
                        testingResultData.setStatus("new");
                        //MdProcessInfoBO:<SITE>,<PROCESS_ID>,<REMARK>
                        testingResultData.setProcessDataBO("MdProcessInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getProcessId() + "," + mDprocessInfo.getRemark());
                        testingResultData.setTimestamp(new Date());
                        //ErpResourceBO:<SITE>,<RESOURCE_ID>
                        testingResultData.setErpResourceBO("ErpResourceBO:" + mDprocessInfo.getSite() + "," + testingMessage.getResourceId());
                        testingResultData.setAlertLevel(alterLe);
                        testingResultData.setDescription("异常数据");
                        testingResultData.setUpLimit(upLimit);
                        testingResultData.setLowLimit(lowLimit);
                        //TxOriginalProcessDataBO:<SITE>,<REMARK>,<SFC> ,<RESOURCE_ID>,<CHANNEL_ID>,<SEQUENCE_ID>
                        testingResultData.setOriginalProcessDataBO("TxOriginalProcessDataBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + testingMessage.getResourceId() + "," + testingMessage.getChannelId() + "，" + testingMessage.getSequenceId());
                        testingResultData.setCreatedDateTime(mDprocessInfo.getCreateDateTime());
                        testingResultData.setCreatedUser(mDprocessInfo.getCreateUser());
                        testingResultData.setModifiedDateTime(mDprocessInfo.getModifiedDateTime());
                        testingResultData.setModifiedUser(mDprocessInfo.getCreateUser());
                        testingResultData.setRootRemark(mDprocessInfo.getRootRemark());
                        testingResultData.setTestingMessage(testingMessage);

                        listResult.add(testingResultData);
                    }
                    break;
                }
            }
        }
        String result = JsonUtils.toJson(listResult);
        jedis.set(key, result);//用于计算过程中查询历史数据
        jedis.expire(key, 60 * 60);
        jedis.sadd("TES-RESULT", result);//用于同步服务写入hana
    }
}