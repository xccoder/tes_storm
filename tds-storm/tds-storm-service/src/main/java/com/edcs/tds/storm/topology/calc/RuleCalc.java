package com.edcs.tds.storm.topology.calc;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.service.CacheService;
import groovy.lang.Binding;
import groovy.lang.Script;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

public class RuleCalc {

    private static final Logger logger = LoggerFactory.getLogger(RuleCalc.class);

    public void TestingRuleCalc(ScriptExecutor scriptExecutor, ExecuteContext executeContext, Binding shellContext,
                                ConcurrentMap<String, List<RuleConfig>> ruleConfig, CacheService cacheService) {

        TestingMessage testingMessage = executeContext.getTestingMessage();
        MDStepInfo mdStepInfo = new MDStepInfo();
        MDprocessInfo mDprocessInfo = new MDprocessInfo();
        Jedis jedis = cacheService.getProxyJedisPool().getResource();
        List<TestingResultData> listResult = new ArrayList();
        int matchedCount = 0;
        String categoty = null;
        String alterLevel = null;
        String key = null;
        //遍历每一个场景
        for (Entry<String, List<RuleConfig>> entry : ruleConfig.entrySet()) {
            categoty = entry.getKey().split("_")[1];
            for (RuleConfig rule : entry.getValue()) {
                //遍历一个场景下工步的规则组
                long executeUsedTime = 0;
                long executeBeginTime = System.currentTimeMillis();
                try {
                    // 从缓存中取对应ID的规则脚本开始执行				     //流程的工步ID
                    //从ScriptCacheMapping中查找与当前工步相匹配的脚本
                    Script script = CacheService.getScriptCache().get(rule.getStepId()).getRight();
                    //					最大量程
                    script.setProperty("IMaxRange", testingMessage.getSvIcRange());
//					 电流
                    script.setProperty("I", testingMessage.getPvCurrent());
                    //截止电流
                    script.setProperty("IEnd", mdStepInfo.getSvStepEndCurrent());
                    script.setProperty("P", mdStepInfo.getSvPower());
                    script.setProperty("UEnd", mdStepInfo.getSvStepEndVoltage());

                    script.setBinding(shellContext);
                    alterLevel = scriptExecutor.execute(script);
                } catch (Exception e) {
                    executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                    logger.error("Rule execute error, rule id: {}." + e, rule.getId());
                    executeContext.addRuleException(rule, e, executeUsedTime);
                } finally {
                    executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                }
                if (StringUtils.isNotBlank(alterLevel) && !alterLevel.equals("null")) {
                    matchedCount++;
                    // TODO 记录匹配的规则和相关数据
                    executeContext.addMatchedRule(rule.getId(), 0d, executeUsedTime);
                    // TODO 将结果集写入Redis缓存
                    BigDecimal upLimit = BigDecimal.valueOf(Long.valueOf(alterLevel.split("_")[1]));//报警上限
                    BigDecimal lowLimit = BigDecimal.valueOf(Long.valueOf(alterLevel.split("_")[2]));//报警下限
//      key = 流程号+工步号+序号+业务循环号
                    key = mdStepInfo.getRemark() + testingMessage.getSequenceId();
                    String handle = "TxAlertInfoBO:" + mdStepInfo.getSite() + "," + mdStepInfo.getRemark() + "," + testingMessage.getSfc() + "," + categoty;
                    TestingResultData testingResultData = new TestingResultData();
                    testingResultData.setHandle(handle);
                    testingResultData.setSite(mdStepInfo.getSite());
                    testingResultData.setCategory(categoty);
                    testingResultData.setAltetSequenceNumber(0);
//                    TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>
                    testingResultData.setTxAlertListInfoBO("TxAlertInfoBO:" + mdStepInfo.getSite() + "," + mdStepInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + categoty);
                    testingResultData.setStatus("new");
//                    MdProcessInfoBO:<SITE>,<PROCESS_ID>,<REMARK>
                    testingResultData.setProcessDataBO("MdProcessInfoBO:" + mdStepInfo.getSite() + "," + mDprocessInfo.getProcessID() + "," + mDprocessInfo.getRemark());
                    testingResultData.setTimestamp(testingMessage.getTimestamp());
//ErpResourceBO:<SITE>,<RESOURCE_ID>
                    testingResultData.setErpResourceBO("ErpResourceBO:" + mdStepInfo.getSite() + "," + testingMessage.getResourceId());
                    testingResultData.setAlertLevel(alterLevel);
                    testingResultData.setDescription("异常数据");
                    testingResultData.setUpLimit(upLimit);
                    testingResultData.setLowLimit(lowLimit);
//                    TxOriginalProcessDataBO:<SITE>,<REMARK>,<SFC> ,<RESOURCE_ID>,<CHANNEL_ID>,<SEQUENCE_ID>
                    testingResultData.setOriginalProcessDataBO("TxOriginalProcessDataBO:" + mdStepInfo.getSite() + "," + mdStepInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + testingMessage.getResourceId() + "," + testingMessage.getChannelId() + "，" + testingMessage.getSequenceId());
                    testingResultData.setCreatedDateTime(mDprocessInfo.getCreateDateTime());
                    testingResultData.setCreatedUser(mDprocessInfo.getCreateUser());
                    testingResultData.setModifiedDateTime(mDprocessInfo.getModifiedDateTime());
                    testingResultData.setModifiedUser(mDprocessInfo.getCreateUser());
                    testingResultData.setRootRemark(mDprocessInfo.getRootRemark());
                    testingResultData.setTestingMessage(testingMessage);

                    listResult.add(testingResultData);
                }
            }
        }
        String result = JsonUtils.toJson(listResult);
        jedis.set(key, result);
    }
}