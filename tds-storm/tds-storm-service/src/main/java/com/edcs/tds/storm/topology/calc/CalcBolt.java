package com.edcs.tds.storm.topology.calc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.storm.Config;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.service.CacheService;
import com.edcs.tds.storm.service.DataInit;
import com.edcs.tds.storm.service.DataService;
import com.edcs.tds.storm.util.RestUtils;
import com.edcs.tds.storm.util.StormBeanFactory;


import groovy.lang.Binding;
import redis.clients.jedis.Jedis;

public class CalcBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(CalcBolt.class);

    private static final long serialVersionUID = 5443752882009732861L;

    protected OutputCollector collector;
    protected String topologyName;
    protected StormBeanFactory beanFactory;
    protected CacheService cacheService;
//    protected BeanSerializer beanSerializer;
//    protected MessageRepeatFilter messageRepeatFilter;
    protected ScriptExecutor scriptExecutor;
//    protected EngineCommonService engineCommonService;
    protected DataService dataService;

    @SuppressWarnings({"rawtypes"})
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.topologyName = (String) stormConf.get(Config.TOPOLOGY_NAME);
        this.beanFactory = new StormBeanFactory(stormConf);
        
//        this.beanSerializer = new BeanSerializer();//对象序列化的工具类

//        this.cacheService = beanFactory.getBean(CacheService.class);
//        this.messageRepeatFilter = beanFactory.getBean(MessageRepeatFilter.class);
        this.scriptExecutor = beanFactory.getBean(ScriptExecutor.class);
//        this.engineCommonService = beanFactory.getBean(EngineCommonService.class);
        this.dataService = beanFactory.getBean(DataService.class);
        this.cacheService = beanFactory.getBean(CacheService.class);
        this.cacheService.start();

        logger.info("The {} blot is prepared..", topologyName);

    }

    @Override
    public void execute(Tuple input) {
        try {
            process(input);
        } finally {
            collector.ack(input);
        }
    }

    @SuppressWarnings("unchecked")
    private void process(Tuple input) {
        ExecuteContext executeContext = new ExecuteContext();

        RuleCalc calc = new RuleCalc();
        Binding shellContext = new Binding();

        boolean isRepeated = false;
        try {
            // TODO 解析请求数据
            logger.info("Start parsing request data...");
            TestingMessage testingMessage = DataInit.initRequestMessage(input);
            // TODO 消息重复消费过滤
            isRepeated = repeatFilter(testingMessage.getMessageId());
            if (isRepeated) {
                return;
            }
            // TODO 实现对异常的循环次数校验、处理
            int currentCycle = dataService.getCurrentCycleCount(testingMessage.getRemark(), cacheService); // TODO 从Redis读取
            int newCycle = currentCycle + 1;
            Jedis jedis = cacheService.getProxyJedisPool().getResource();
            if (currentCycle != -1) {
                int cycle = testingMessage.getCycle();
                if (newCycle != cycle) {
                    //更新Redis
                    jedis.set(testingMessage.getRemark(), newCycle + "");
                    //更新business_cycle
                    testingMessage.setBusinessCycle(newCycle);
                }
            } else {
                //更新Redis
                jedis.set(testingMessage.getRemark(), newCycle + "");
                //TODO 更新business_cycle
//				testingMessage.setBusinessCycle(currentCycle);
            }
            //修改测试数据中的业务循环号（businessCycle）
            testingMessage = dataService.updateBusinessCycle(testingMessage, cacheService);
            //维护工步的逻辑序号
            testingMessage = dataService.updateStepLogicNumber(testingMessage,cacheService);
            
            executeContext.setDebug(testingMessage.isDebug());
            executeContext.setTestingMessage(testingMessage);
            
            
            // TODO 加载主数据  CacheService.getDataXxxx();
            // 初始化 ShellContext
//          DataInit.initShellContext(testingMessage, engineCommonService, executeContext, shellContext);
            shellContext = DataInit.initShellContext(executeContext, cacheService, shellContext);
            // 开始规则计算匹配
            ConcurrentMap<String, List<RuleConfig>> ruleConfig = CacheService.getRuleConfig();
            // 核心计算
            calc.TestingRuleCalc(scriptExecutor, executeContext, shellContext, ruleConfig,cacheService);
            // 告知redis此流程已经结束
            int workType = testingMessage.getPvWorkType();//假设2代表流程结束标志，到时候根据实际数据更改
            if (workType == 0) {
                //TODO 调用redis接口去通知此流程已经结束
            	Map<String,Object> map = new HashMap<String,Object>();
            	map.put("remark", testingMessage.getRemark());
            	map.put("txStatus", "close");
            	map.put("site", "1000");
            	map.put("totalCycleNum", testingMessage.getBusinessCycle());
            	String json = JsonUtils.toJson(map);		
            	String url = "http://172.26.66.35:50000/tes-backing/api/v1/integration/storm/md_process_info";
            	RestUtils.sendState(url, json);
            }
            executeContext.setSysVariableLog(shellContext.getVariables());
        } catch (Exception e) {
            logger.error("", e);
            executeContext.addException(e);
        } finally {
            shellContext = null;
        }
    }

    public boolean repeatFilter(String messageId) {
        // TODO
        // if (onceFilterService.filter("AUTH", messageId)) {
        // logger.info("AuthRuleBolt filter the repetitive tuple,messagedId is
        // {}.", messageId);
        // return true;
        // }
        return false;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // NOTHING_TO_DO
    }
    
    public static void main(String[] args) {
    	Map<String,Object> map = new HashMap<String,Object>();
    	map.put("remark", "T3-20170324-3495-603301_RT-DC-POWER_1_F100C0-BF_0_2");
    	map.put("txStatus", "close");
    	map.put("site", "1000");
    	map.put("totalCycleNum", 10);
    	String json = JsonUtils.toJson(map);		
    	String url = "http://172.26.66.35:50000/tes-backing/api/v1/integration/storm/md_process_info";
    	RestUtils.sendState(url, json);
	}

}
