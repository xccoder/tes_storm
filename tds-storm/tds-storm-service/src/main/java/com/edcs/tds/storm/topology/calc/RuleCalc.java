package com.edcs.tds.storm.topology.calc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.model.EmailEntity;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.common.util.SendEmailUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.model.UserIntegrationRedis;
import com.edcs.tds.storm.service.CacheService;

import groovy.lang.Binding;
import groovy.lang.Script;
import redis.clients.jedis.Jedis;

public class RuleCalc {
	
	public static final String myEmailAccount = "TDS-Admin@catlbattery.com";
    public static final String myEmailPassword = "Aa123456";
    public static final String myEmailSMTPHost = "Mail.catlbattery.com";

    private static final Logger logger = LoggerFactory.getLogger(RuleCalc.class);

    public void TestingRuleCalc(ScriptExecutor scriptExecutor, ExecuteContext executeContext, Binding shellContext,
                                ConcurrentMap<String, List<RuleConfig>> ruleConfig, CacheService cacheService) {

        TestingMessage testingMessage = executeContext.getTestingMessage();//实时数据
        Set<String> processInfoJsons = cacheService.getProcessInfoJsons();//获取流程主数据 信息
        MDprocessInfo mDprocessInfo = null;
		if(processInfoJsons!=null && processInfoJsons.size()>0){
			for (String string : processInfoJsons) {
				MDprocessInfo mDprocessInfo2 = JsonUtils.toObject(string, MDprocessInfo.class);
				if (testingMessage.getRemark().equals(mDprocessInfo2.getRemark())) {
	                //获取当前测试的流程
	                mDprocessInfo = mDprocessInfo2;
	                break;
	            }
			}
		}else{
			return;
		}
        Jedis jedis = cacheService.getProxyJedisPool().getResource();
        List<TestingResultData> listResult = new ArrayList<TestingResultData>();//用来存放结果数据
        //int matchedCount = 0;
        // key = 流程号+序号
        String key = testingMessage.getRemark() +"_"+testingMessage.getSequenceId();
        int sequenceNumber = 0;//同一个工步中报警的序号
        //遍历每一个场景
        Set<String> keys = ruleConfig.keySet();//获取所有的key 的值
        for (String string : keys) {
        	sequenceNumber++;
			List<RuleConfig> rule = ruleConfig.get(string);//获取每一个场景的值
			String str = string.substring(string.lastIndexOf("_")+1);
			String sceneName = str;//场景名称
			for (RuleConfig ruleConfig2 : rule) {//遍历一个场景下的所有规则
				if(testingMessage.getStepName().equals(ruleConfig2.getStepName())){
					long executeUsedTime = 0;
	                long executeBeginTime = System.currentTimeMillis();
	                String alterLevel = null;
                    try {
                    	Script script = CacheService.getScriptCache().get(ruleConfig2.getStepSign()).getRight();
                    	script.setBinding(shellContext);
                        // 返回值为 ： 报警上限_报警下限_比较值_报警级别
                        alterLevel = scriptExecutor.execute(script);
                    } catch (Exception e) {
                        executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                        logger.error("Rule execute error, rule id: {}." + e, ruleConfig2.getId());
                        executeContext.addRuleException(ruleConfig2, e, executeUsedTime);
                    } finally {
                        executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                    }
                    if (StringUtils.isNotBlank(alterLevel) && !alterLevel.equals("null")) {
                    	TestingResultData testingResultData = new TestingResultData();
                        //matchedCount++;
                        // TODO 记录匹配的规则和相关数据
//                        executeContext.addMatchedRule(ruleConfig2.getId(), 0d, executeUsedTime);
                        // TODO 将结果集写入Redis缓存
                        String[] alters = alterLevel.split("_");
                        BigDecimal upLimit = new BigDecimal(alters[0]);    //.valueOf(Long.valueOf(alters[0]));//报警上限
                        BigDecimal lowLimit = new  BigDecimal(alters[1]); //.valueOf(Long.valueOf(alters[1]));//报警下限
                        int alterLe = Integer.parseInt(alters[3]);//报警级别
                        //调用发送邮件接口发送预警信息  -- start
                        //通过redis获取收件人信息
                        Set<String> sets = jedis.smembers("warningLevel_"+alterLe);
                        
//                        Set<String> sets = new HashSet<String>();
//                        sets.add("XiangC@CATLBattery.com");
//                        sets.add("CaiSL2@CATLBattery.com");
//                        sets.add("LiQF@CATLBattery.com");
                        String content = "通道号为："+testingMessage.getChannelId()+";</br>流程号为："+testingMessage.getRemark()+";</br>公布名称为："+ruleConfig2.getStepName()+";</br>场景名称为："+sceneName+";</br>产生了"+alterLe+"级预警！！";//预警信息。
                        System.out.println("报警信息为："+content+"-----------------------------------------------------------");
                        if(sets!=null && sets.size()>0){
                        	List<String> receiveAccounts = new ArrayList<String>();//存放收件人帐号
                        	EmailEntity emailEntity = new EmailEntity();
                        	for (String string2 : sets) {
                        		UserIntegrationRedis userMsg = JsonUtils.toObject(string2, UserIntegrationRedis.class);
                        		receiveAccounts.add(userMsg.getEmail());
                        	}
                        	receiveAccounts.addAll(sets);
                        	emailEntity.setReceiveAccounts(receiveAccounts);
                        	emailEntity.setBooeanSsl(false);
                        	emailEntity.setContent(content);
                        	
                        	emailEntity.setSendServer(myEmailSMTPHost);
                        	emailEntity.setSendAccount(myEmailAccount);
                        	emailEntity.setEmailPassword(myEmailPassword);
                        	//发送邮件
                        	try {
								SendEmailUtils.sendEmail(emailEntity);
							} catch (Exception e) {
								e.printStackTrace();
							}
                        }
                        //调用发送邮件接口发送预警信息  -- end

                        
                        String handle = "TxAlertInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + testingMessage.getSfc() + "," + sceneName;
                        
                        testingResultData.setHandle(handle);
                        testingResultData.setSite(mDprocessInfo.getSite());
                        testingResultData.setCategory(sceneName);
                        testingResultData.setAltetSequenceNumber(sequenceNumber);
                        //TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>
                        //testingResultData.setTxAlertListInfoBO("TxAlertInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + sceneName);
                        testingResultData.setStatus("new");
                        //MdProcessInfoBO:<SITE>,<PROCESS_ID>,<REMARK>
                        testingResultData.setProcessDataBO("MdProcessInfoBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getProcessId() + "," + mDprocessInfo.getRemark());
                        testingResultData.setTimestamp(new Timestamp(new Date().getTime()));
                        //ErpResourceBO:<SITE>,<RESOURCE_ID>
                        testingResultData.setErpResourceBO("ErpResourceBO:" + mDprocessInfo.getSite() + "," + testingMessage.getResourceId());
                        testingResultData.setAlertLevel(alterLe);
                        testingResultData.setDescription(content);
                        testingResultData.setUpLimit(upLimit);
                        testingResultData.setLowLimit(lowLimit);
                        //TxOriginalProcessDataBO:<SITE>,<REMARK>,<SFC> ,<RESOURCE_ID>,<CHANNEL_ID>,<SEQUENCE_ID>
                        testingResultData.setOriginalProcessDataBO("TxOriginalProcessDataBO:" + mDprocessInfo.getSite() + "," + mDprocessInfo.getRemark() + "," + mDprocessInfo.getSfc() + "," + testingMessage.getResourceId() + "," + testingMessage.getChannelId() + "," + testingMessage.getSequenceId());
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
        if(listResult.size()==0){//表明这条测试数据一个预警信息都没有，那么我们要产生一个result对象来存放原始测试数据信息
        	TestingResultData testingResultData = new TestingResultData();
        	testingResultData.setTestingMessage(testingMessage);
        	listResult.add(testingResultData);
        }
        String result = JsonUtils.toJson(listResult);
        jedis.set(key, result);//用于计算过程中查询历史数据
        jedis.expire(key, 60 * 60);
        jedis.sadd("TES-RESULT", result);//用于同步服务写入hana
    }
}