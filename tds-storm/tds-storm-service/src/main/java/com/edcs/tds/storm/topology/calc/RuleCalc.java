package com.edcs.tds.storm.topology.calc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.TestingMessage;
import com.edcs.tds.storm.service.CacheService;

import groovy.lang.Binding;
import groovy.lang.Script;

public class RuleCalc {


    protected final Logger logger = LoggerFactory.getLogger(RuleCalc.class);

    public void TestingRuleCalc(ScriptExecutor scriptExecutor, ExecuteContext executeContext, Binding shellContext,
                                ConcurrentMap<String, List<RuleConfig>> ruleConfig) {

        TestingMessage testingMessage = executeContext.getTestingMessage();
        int matchedCount = 0;

        for (Entry<String, List<RuleConfig>> entry : ruleConfig.entrySet()) {
            //遍历每一个场景
            for (RuleConfig rule : entry.getValue()) {
                //遍历一个场景下工步的规则组
                boolean ruleIsMatched = false;
                long executeUsedTime = 0;
                long executeBeginTime = System.currentTimeMillis();

                try {
                    // 从缓存中取对应ID的规则脚本开始执行				     //流程的工步ID
                    //从ScriptCacheMapping中匹配与当前工步相匹配的脚本
                    Script script = CacheService.getScriptCache().get(rule.getStepId()).getRight();
                    ruleIsMatched = scriptExecutor.execute(script);
//					最大量程
                    script.setProperty("IMaxRange", testingMessage.getSvIcRange());
//					 电流
                    script.setProperty("I", testingMessage.getPvCurrent());
                    script.setBinding(shellContext);
                } catch (Exception e) {
                    executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                    logger.error("Rule execute error, rule id: {}." + e, rule.getId());
                    executeContext.addRuleException(rule, e, executeUsedTime);
                } finally {
                    executeUsedTime = System.currentTimeMillis() - executeBeginTime;
                }
                if (ruleIsMatched) {
                    matchedCount++;
                    // TODO 记录匹配的规则和相关数据
                    executeContext.addMatchedRule(rule.getId(), 0d, executeUsedTime);
                }
            }
        }

        // TODO 将结果集写入Redis缓存

    }


}