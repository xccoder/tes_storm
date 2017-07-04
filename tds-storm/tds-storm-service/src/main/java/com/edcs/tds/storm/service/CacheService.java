package com.edcs.tds.storm.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.engine.groovy.ScriptCacheMapping;
import com.edcs.tds.common.engine.groovy.ScriptExecutor;
import com.edcs.tds.common.model.RuleConfig;
import com.edcs.tds.common.redis.JedisFactory;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.redis.RedisCacheKey;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.model.VariableDict;
import com.edcs.tds.storm.util.DataSerializer;
import com.google.common.collect.Maps;

import groovy.lang.Script;
import redis.clients.jedis.Jedis;

/**
 * 核心规则计算数据缓存服务
 *
 * @author willian
 */
public class CacheService {

    private final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private ProxyJedisPool proxyJedisPool;

    private static AtomicReference<VariableDict> variableDict = new AtomicReference<VariableDict>();

    private static ConcurrentMap<String, List<RuleConfig>> ruleConfig = Maps.newConcurrentMap();

    private static Vector<String> ruleIds = new Vector<String>();


    private static ScriptCacheMapping scriptCacheMapping = new ScriptCacheMapping();

    private static volatile boolean inited = false;

    private static volatile Thread updateThread;

    private String ruleConfigVersion;

    private String processInfoJson;//存流程信息

    public String getProcessInfoJson() {
        return processInfoJson;
    }

    public void setProcessInfoJson(String processInfoJson) {
        this.processInfoJson = processInfoJson;
    }

    public String getRuleConfigVersion() {
        return ruleConfigVersion;
    }

    public void setRuleConfigVersion(String ruleConfigVersion) {
        this.ruleConfigVersion = ruleConfigVersion;
    }

    public void setProxyJedisPool(ProxyJedisPool proxyJedisPool) {
        this.proxyJedisPool = proxyJedisPool;
    }
    public ProxyJedisPool getProxyJedisPool() {
		return proxyJedisPool;
	}

    public static VariableDict getVariableDict() {
        return variableDict.get();
    }

    public static ConcurrentMap<String, Pair<String, Script>> getScriptCache() {
        return scriptCacheMapping.getScriptCache();
    }

    public static ConcurrentMap<String, List<RuleConfig>> getRuleConfig() {
        return ruleConfig;
    }

    public static int getRuleConfigLength() {
        int count = 0;
        for (List<RuleConfig> rules : ruleConfig.values()) {
            count += rules.size();
        }
        return count;
    }

    public synchronized Object asObject(byte[] b) {
        try {
            return DataSerializer.getJsonFSTConfiguration().asObject(b);
        } catch (Exception e) {
            try {
                throw new RuntimeException(new String(b, "UTF-8"), e);
            } catch (UnsupportedEncodingException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    /**
     * 初始化数据
     */
    public void init() {
        synchronized (CacheService.class) {
            if (inited) {
                return;
            }
            logger.info("init TDS topology data cache...");
            initRuleConfig();
            cleanInvalidCache();
            inited = true;
        }
    }

    public void start() {
        synchronized (CacheService.class) {
            if (updateThread != null) {
                return;
            }
            String name = "TDS-CacheService-Update" + new Random().nextInt(100);
            updateThread = new UpdateThread(name);
            updateThread.start();
        }
    }

    /**
     * 更新线程
     *
     * @author willian
     */
    private class UpdateThread extends Thread {

        public UpdateThread(String threadName) {
            super.setName(threadName);
        }

        @Override
        public void run() {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(60);
                } catch (InterruptedException e1) {
                }
                logger.info("Begin update IDV topology data cache...");
                try {
                    initRuleConfig();
                    cleanInvalidCache();
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    /**
     * 初始化规则配置
     */
    public void initRuleConfig() {
        Jedis jedis = null;

        try {
            jedis = proxyJedisPool.getResource();

            String newMdProcessjosn = jedis.get(RedisCacheKey.getMDProcessKey());
            if (!StringUtils.equals(newMdProcessjosn, processInfoJson)) {
                //Preconditions.checkNotNull(data);
                ruleConfig.clear();
                ruleIds.clear();
                List<MDprocessInfo> mDprocessInfos = JsonUtils.toArray(newMdProcessjosn, MDprocessInfo.class);

                for (MDprocessInfo mDprocessInfo : mDprocessInfos) {

                    List<MDStepInfo> mdStepInfos = mDprocessInfo.getMdStepInfoList();

                    List<RuleConfig> ruleConfigCurrlist = new ArrayList<>();//电流场景
                    List<RuleConfig> ruleConfigTemplist = new ArrayList<>();//温度场景
                    List<RuleConfig> ruleConfigCapalist = new ArrayList<>();//容量场景
                    List<RuleConfig> ruleConfigTimelist = new ArrayList<>();//时间场景
                    List<RuleConfig> ruleConfigVoltlist = new ArrayList<>();//电压场景
                    List<RuleConfig> ruleConfigEnerlist = new ArrayList<>();//能量场景
                    //将每一条工步数据的每个脚本给ruleconfig对象;
                    for (MDStepInfo mDstepInfo : mdStepInfos) {
                        if (mDstepInfo.getScriptCapacity() != null) {
                            RuleConfig ruleConfCapacity = new RuleConfig();
                            ruleConfCapacity.setRuleScript(mDstepInfo.getScriptCapacity());
                            ruleConfCapacity.setHashcode(mDstepInfo.getScriptCapacityhash());
                            ruleConfCapacity.setRuleGroup(1L);
                            ruleConfCapacity.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfigCapalist.add(ruleConfCapacity);
                            cacheScript(ruleConfCapacity.getStepId(), ruleConfCapacity.getRuleScript(), ruleConfCapacity.getHashcode());
                            ruleIds.add(ruleConfCapacity.getStepId());

                        }
                        if (mDstepInfo.getScriptCurrent() != null) {
                            RuleConfig ruleConfCurrent = new RuleConfig();
                            ruleConfCurrent.setRuleScript(mDstepInfo.getScriptCurrent());
                            ruleConfCurrent.setHashcode(mDstepInfo.getScriptCurrenthash());
                            ruleConfCurrent.setRuleGroup(1L);
                            ruleConfCurrent.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfigCurrlist.add(ruleConfCurrent);
                            cacheScript(ruleConfCurrent.getStepId(), ruleConfCurrent.getRuleScript(), ruleConfCurrent.getHashcode());
                            ruleIds.add(ruleConfCurrent.getStepId());
                        }
                        if (mDstepInfo.getScriptTemperature() != null) {
                            RuleConfig ruleConfTemperature = new RuleConfig();
                            ruleConfTemperature.setRuleScript(mDstepInfo.getScriptTemperature());
                            ruleConfTemperature.setHashcode(mDstepInfo.getScriptTemperaturehash());
                            ruleConfTemperature.setRuleGroup(1L);
                            ruleConfTemperature.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfigTemplist.add(ruleConfTemperature);
                            cacheScript(ruleConfTemperature.getStepId(), ruleConfTemperature.getRuleScript(), ruleConfTemperature.getHashcode());
                            ruleIds.add(ruleConfTemperature.getStepId());
                        }
                        if (mDstepInfo.getScriptTime() != null) {
                            RuleConfig ruleConfTime = new RuleConfig();
                            ruleConfTime.setRuleScript(mDstepInfo.getScriptTime());
                            ruleConfTime.setHashcode(mDstepInfo.getScriptTimehash());
                            ruleConfTime.setRuleGroup(1L);
                            ruleConfTime.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfigTimelist.add(ruleConfTime);
                            cacheScript(ruleConfTime.getStepId(), ruleConfTime.getRuleScript(), ruleConfTime.getHashcode());
                            ruleIds.add(ruleConfTime.getStepId());
                        }
                        if (mDstepInfo.getScriptVoltage() != null) {
                            RuleConfig ruleConfVoltage = new RuleConfig();
                            ruleConfVoltage.setRuleScript(mDstepInfo.getScriptVoltage());
                            ruleConfVoltage.setHashcode(mDstepInfo.getScriptVoltagehash());
                            ruleConfVoltage.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfVoltage.setRuleGroup(1L);
                            ruleConfigVoltlist.add(ruleConfVoltage);
                            cacheScript(ruleConfVoltage.getStepId(), ruleConfVoltage.getRuleScript(), ruleConfVoltage.getHashcode());
                            ruleIds.add(ruleConfVoltage.getStepId());
                        }
                        if (mDstepInfo.getScriptEnergy() != null && mDstepInfo.getScriptEnergy().equals("")) {
                            RuleConfig ruleConfEnergy = new RuleConfig();
                            ruleConfEnergy.setRuleScript(mDstepInfo.getScriptEnergy());
                            ruleConfEnergy.setHashcode(mDstepInfo.getScriptEnergyhash());
                            ruleConfEnergy.setRuleGroup(1L);
                            ruleConfEnergy.setStepId(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId()));
                            ruleConfigEnerlist.add(ruleConfEnergy);
                            cacheScript(ruleConfEnergy.getStepId(), ruleConfEnergy.getRuleScript(), ruleConfEnergy.getHashcode());
                            ruleIds.add(ruleConfEnergy.getStepId());
                        }
                        /***将每个流程的各种场景放入scriptcachemapping*/
                    }
                    ruleConfig.put(mDprocessInfo.getProcessID()+"curr", ruleConfigCurrlist);
                    ruleConfig.put(mDprocessInfo.getProcessID()+"capa", ruleConfigCapalist);
                    ruleConfig.put(mDprocessInfo.getProcessID()+"enger", ruleConfigEnerlist);
                    ruleConfig.put(mDprocessInfo.getProcessID()+"temp", ruleConfigTemplist);
                    ruleConfig.put(mDprocessInfo.getProcessID()+"time", ruleConfigTimelist);
                    ruleConfig.put(mDprocessInfo.getProcessID()+"volt", ruleConfigVoltlist);

                }
            }
            processInfoJson = newMdProcessjosn ;
        } finally {
            JedisFactory.closeQuietly(jedis);
        }
       /* try {

         *//*   jedis = proxyJedisPool.getResource();
            String newVersion = jedis.get(RedisCacheKey.getRuleConfigVersion());
            // 判断Redis中的缓存版本，如果有新版本则更新数据
            if (!StringUtils.equals(newVersion, ruleConfigVersion)) {
                byte[] data = jedis.get(RedisCacheKey.getRuleConfig());
                Preconditions.checkNotNull(data);
                ruleConfig.clear();
                ruleIds.clear();

                // 规则更新
                @SuppressWarnings("unchecked")
                List<RuleConfig> rules = (List<RuleConfig>) asObject(data);
                for (RuleConfig rule : rules) {
                    Long id = rule.getId();
                    cacheScript(id, rule.getRuleScript(), rule.getHashcode());

                    Long group = rule.getRuleGroup();
                    if (!ruleConfig.containsKey(group)) {
                        List<RuleConfig> list = Lists.newArrayList();
                        list.add(rule);
                        ruleConfig.put(group, list);
                    } else {
                        ruleConfig.get(group).add(rule);
                    }
                    ruleIds.add(id);
                }

                logger.info("Update IDV topology rule config cache...");
            }
            ruleConfigVersion = newVersion;*//*
        } finally {
            JedisFactory.closeQuietly(jedis);
        }*/
    }

    /**
     * 清理无效的脚本缓存
     */
    public void cleanInvalidCache() {
        if (ruleIds.size() > 0 && scriptCacheMapping.getScriptCache().size() > 0) {
            Set<String> cachedIds = scriptCacheMapping.getScriptCache().keySet();
            for (String id : cachedIds) {
                if (!ruleIds.contains(id)) {
                    logger.info("Remove the invalid script caching, rule id: {}.", id);
                    scriptCacheMapping.remove(id);
                }
            }
        }
    }

    /**
     * 缓存脚本
     *
     * @param id
     * @param scriptContent
     * @param hashcode
     */
    private void cacheScript(String id, String scriptContent, String hashcode) {
        // 判断新的脚本内容与缓存是否有差异，如果有则重新编译
        if (!scriptCacheMapping.isDifference(id, hashcode)) {//id暂定为流程加工步id
            scriptCacheMapping.addScript(id, hashcode, ScriptExecutor.getDefaultShell().parse(scriptContent));
            logger.info("Finding a new script, recompile and cache, rule id: {}.", id);
        }
    }

}