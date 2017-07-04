package com.edcs.tds.storm.topology.calc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.engine.groovy.service.EngineCommonService;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.TestingMessage;
import com.edcs.tds.storm.service.CacheService;
import com.edcs.tds.storm.service.DataInit;
import com.edcs.tds.storm.service.DataService;
import com.edcs.tds.storm.service.MessageRepeatFilter;
import com.edcs.tds.storm.util.BeanSerializer;
import com.edcs.tds.storm.util.StormBeanFactory;

import backtype.storm.Config;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import groovy.lang.Binding;
import redis.clients.jedis.Jedis;

public class CalcBolt extends BaseRichBolt {

    private static final Logger logger = LoggerFactory.getLogger(CalcBolt.class);

    private static final long serialVersionUID = 5443752882009732861L;

    protected OutputCollector collector;
    protected String topologyName;
    protected StormBeanFactory beanFactory;
    protected CacheService cacheService;
    protected BeanSerializer beanSerializer;
    protected MessageRepeatFilter messageRepeatFilter;
    protected ScriptExecutor scriptExecutor;
    protected EngineCommonService enginecommonservice;
    protected DataService dataService;

    @SuppressWarnings({"rawtypes"})
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        this.topologyName = (String) stormConf.get(Config.TOPOLOGY_NAME);
        this.beanFactory = new StormBeanFactory(stormConf);
        this.beanSerializer = new BeanSerializer();

        this.cacheService = beanFactory.getBean(CacheService.class);
        this.messageRepeatFilter = beanFactory.getBean(MessageRepeatFilter.class);
        this.scriptExecutor = beanFactory.getBean(ScriptExecutor.class);
        this.enginecommonservice = beanFactory.getBean(EngineCommonService.class);
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
            //修改测试数据中的业务循环号（businessCycle）
            testingMessage = dataService.updateBusinessCycle(testingMessage, cacheService);

            executeContext.setDebug(testingMessage.isDebug());
            executeContext.setTestingMessage(testingMessage);
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

            // TODO 加载主数据  CacheService.getDataXxxx();
            //
            // 初始化 ShellContext
            DataInit.initShellContext(testingMessage, enginecommonservice, executeContext, shellContext);

            // 开始规则计算匹配
            ConcurrentMap<String, List<RuleConfig>> ruleConfig = CacheService.getRuleConfig();
            // 核心计算
            calc.TestingRuleCalc(scriptExecutor, executeContext, shellContext, ruleConfig);
            String workType = testingMessage.getPvWorkType();
            if (StringUtils.isNotBlank(workType) && workType.equals("已完成")) {
                //TODO 调用redis接口去通知此流程已经结束
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

}
