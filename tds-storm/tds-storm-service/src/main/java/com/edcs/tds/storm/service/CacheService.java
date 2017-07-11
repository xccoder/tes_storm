package com.edcs.tds.storm.service;

import java.io.UnsupportedEncodingException;
import java.util.*;
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
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.model.VariableDict;
import com.edcs.tds.storm.util.DataSerializer;
import com.google.common.collect.Maps;

import groovy.lang.Script;
import redis.clients.jedis.Jedis;
import scala.util.parsing.combinator.testing.Str;

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
    private Set<String> processInfoJsons;
    private boolean flag = false;
    private DBHelperUtils dbUtils;//连接hana的工具类

    public Set<String> getProcessInfoJsons() {
        return processInfoJsons;
    }

    public void setProcessInfoJsons(Set<String> processInfoJsons) {
        this.processInfoJsons = processInfoJsons;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

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

    public DBHelperUtils getDbUtils() {
		return dbUtils;
	}
    public void setDbUtils(DBHelperUtils dbUtils) {
		this.dbUtils = dbUtils;
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
        Jedis jedis = proxyJedisPool.getResource();
        System.out.println("开始》》》》》》》》》》》》");
        try {
            //jedis = proxyJedisPool.getResource();
            Set<String> MdProcessjsons = jedis.smembers(" ");
            if (compareSet(MdProcessjsons, processInfoJsons)){
                ruleConfig.clear();
                ruleIds.clear();
                for (String str : MdProcessjsons){
                    MDprocessInfo mDprocessInfo = JsonUtils.toObject(str, MDprocessInfo.class);
                    List<MDStepInfo> mdStepInfos = mDprocessInfo.getMdStepInfoList();
                    System.out.println("mdStepInfos的长度"+mdStepInfos.size());
                    List<RuleConfig> ruleConfigCurrlist = new ArrayList<>();//电流场景
                    List<RuleConfig> ruleConfigTemplist = new ArrayList<>();//温度场景
                    List<RuleConfig> ruleConfigCapalist = new ArrayList<>();//容量场景
                    List<RuleConfig> ruleConfigTimelist = new ArrayList<>();//时间场景
                    List<RuleConfig> ruleConfigVoltlist = new ArrayList<>();//电压场景
                    List<RuleConfig> ruleConfigEnerlist = new ArrayList<>();//能量场景
                    //将每一条工步数据的每个脚本给ruleconfig对象;
                    for (MDStepInfo mDstepInfo : mdStepInfos) {
                        System.out.println(mDstepInfo.getRemark());
                        if (mDstepInfo.getScriptCapacity() != null) {
                            RuleConfig ruleConfCapacity = new RuleConfig();
                            ruleConfCapacity.setRuleScript(mDstepInfo.getScriptCapacity());
                            ruleConfCapacity.setHashcode(mDstepInfo.getScriptCapacityHash());
                            ruleConfCapacity.setRuleGroup(1L);
                            ruleConfCapacity.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"capacity");//规则的流程加工步号
                            ruleConfCapacity.setStepName(mDstepInfo.getStepName());
                            ruleConfigCapalist.add(ruleConfCapacity);
                            /**缓存的id为流程号加工步号*/
                            cacheScript(ruleConfCapacity.getStepSign(), ruleConfCapacity.getRuleScript(), ruleConfCapacity.getHashcode());
                            ruleIds.add(ruleConfCapacity.getStepSign());

                        }
                        if (mDstepInfo.getScriptCurrent() != null) {
                            RuleConfig ruleConfCurrent = new RuleConfig();
                            ruleConfCurrent.setRuleScript(mDstepInfo.getScriptCurrent());
                            ruleConfCurrent.setHashcode(mDstepInfo.getScriptCurrentHash());
                            ruleConfCurrent.setRuleGroup(1L);
                            ruleConfCurrent.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"current");
                            ruleConfCurrent.setStepName(mDstepInfo.getStepName());
                            ruleConfigCurrlist.add(ruleConfCurrent);
                            cacheScript(ruleConfCurrent.getStepSign(), ruleConfCurrent.getRuleScript(), ruleConfCurrent.getHashcode());
                            ruleIds.add(ruleConfCurrent.getStepSign());
                        }
                        if (mDstepInfo.getScriptTemperature() != null) {
                            RuleConfig ruleConfTemperature = new RuleConfig();
                            ruleConfTemperature.setRuleScript(mDstepInfo.getScriptTemperature());
                            ruleConfTemperature.setHashcode(mDstepInfo.getScriptTemperatureHash());
                            ruleConfTemperature.setRuleGroup(1L);
                            ruleConfTemperature.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"temp");
                            ruleConfTemperature.setStepName(mDstepInfo.getStepName());
                            ruleConfigTemplist.add(ruleConfTemperature);
                            cacheScript(ruleConfTemperature.getStepSign(), ruleConfTemperature.getRuleScript(), ruleConfTemperature.getHashcode());
                            ruleIds.add(ruleConfTemperature.getStepSign());
                        }
                        if (mDstepInfo.getScriptTime() != null) {
                            RuleConfig ruleConfTime = new RuleConfig();
                            ruleConfTime.setRuleScript(mDstepInfo.getScriptTime());
                            ruleConfTime.setHashcode(mDstepInfo.getScriptTimeHash());
                            ruleConfTime.setRuleGroup(1L);
                            ruleConfTime.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"time");
                            ruleConfTime.setStepName(mDstepInfo.getStepName());
                            ruleConfigTimelist.add(ruleConfTime);
                            cacheScript(ruleConfTime.getStepSign(), ruleConfTime.getRuleScript(), ruleConfTime.getHashcode());
                            ruleIds.add(ruleConfTime.getStepSign());
                        }
                        if (mDstepInfo.getScriptVoltage() != null) {
                            RuleConfig ruleConfVoltage = new RuleConfig();
                            ruleConfVoltage.setRuleScript(mDstepInfo.getScriptVoltage());
                            ruleConfVoltage.setHashcode(mDstepInfo.getScriptVoltageHash());
                            ruleConfVoltage.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"volt");
                            ruleConfVoltage.setStepName(mDstepInfo.getStepName());
                            ruleConfVoltage.setRuleGroup(1L);
                            ruleConfigVoltlist.add(ruleConfVoltage);
                            cacheScript(ruleConfVoltage.getStepSign(), ruleConfVoltage.getRuleScript(), ruleConfVoltage.getHashcode());
                            ruleIds.add(ruleConfVoltage.getStepSign());
                        }
                        if (mDstepInfo.getScriptEnergy() != null && mDstepInfo.getScriptEnergy().equals("")) {
                            RuleConfig ruleConfEnergy = new RuleConfig();
                            ruleConfEnergy.setRuleScript(mDstepInfo.getScriptEnergy());
                            ruleConfEnergy.setHashcode(mDstepInfo.getScriptEnergyHash());
                            ruleConfEnergy.setRuleGroup(1L);
                            ruleConfEnergy.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"energy");
                            ruleConfEnergy.setStepName(mDstepInfo.getStepName());
                            ruleConfigEnerlist.add(ruleConfEnergy);
                            cacheScript(ruleConfEnergy.getStepSign(), ruleConfEnergy.getRuleScript(), ruleConfEnergy.getHashcode());
                            ruleIds.add(ruleConfEnergy.getStepSign());
                        }
                        /***将每个流程的各种场景放入scriptcachemapping*/
                    }

                    ruleConfig.put(mDprocessInfo.getProcessId()+"_curr", ruleConfigCurrlist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_capa", ruleConfigCapalist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_enger", ruleConfigEnerlist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_temp", ruleConfigTemplist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_time", ruleConfigTimelist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_volt", ruleConfigVoltlist);
                    System.out.println("ruleConfig长度："+ruleConfig.size());
                    System.out.println("ruleIds长度："+ruleIds.size());
                }
            }
            processInfoJsons = MdProcessjsons ;
           /* if (!StringUtils.equals(newMdProcessjosn, processInfoJson)) {
                //Preconditions.checkNotNull(data);
                ruleConfig.clear();
                ruleIds.clear();
                System.out.println("ruleIds清除后长度："+ruleIds.size());
                List<MDprocessInfo> mDprocessInfos = JsonUtils.toArray(newMdProcessjosn, MDprocessInfo.class);
                System.out.println("mDprocessInfos的长度"+mDprocessInfos.size());
                for (MDprocessInfo mDprocessInfo : mDprocessInfos) {

                    List<MDStepInfo> mdStepInfos = mDprocessInfo.getMdStepInfoList();
                    System.out.println("mdStepInfos的长度"+mdStepInfos.size());
                    List<RuleConfig> ruleConfigCurrlist = new ArrayList<>();//电流场景
                    List<RuleConfig> ruleConfigTemplist = new ArrayList<>();//温度场景
                    List<RuleConfig> ruleConfigCapalist = new ArrayList<>();//容量场景
                    List<RuleConfig> ruleConfigTimelist = new ArrayList<>();//时间场景
                    List<RuleConfig> ruleConfigVoltlist = new ArrayList<>();//电压场景
                    List<RuleConfig> ruleConfigEnerlist = new ArrayList<>();//能量场景
                    //将每一条工步数据的每个脚本给ruleconfig对象;
                    for (MDStepInfo mDstepInfo : mdStepInfos) {
                        System.out.println(mDstepInfo.getRemark());
                        if (mDstepInfo.getScriptCapacity() != null) {
                            RuleConfig ruleConfCapacity = new RuleConfig();
                            ruleConfCapacity.setRuleScript(mDstepInfo.getScriptCapacity());
                            ruleConfCapacity.setHashcode(mDstepInfo.getScriptCapacityHash());
                            ruleConfCapacity.setRuleGroup(1L);
                            ruleConfCapacity.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"capacity");//规则的流程加工步号
                            ruleConfCapacity.setStepName(mDstepInfo.getStepName());
                            ruleConfigCapalist.add(ruleConfCapacity);
                            *//**缓存的id为流程号加工步号*//*
                            cacheScript(ruleConfCapacity.getStepSign(), ruleConfCapacity.getRuleScript(), ruleConfCapacity.getHashcode());
                            ruleIds.add(ruleConfCapacity.getStepSign());

                        }
                        if (mDstepInfo.getScriptCurrent() != null) {
                            RuleConfig ruleConfCurrent = new RuleConfig();
                            ruleConfCurrent.setRuleScript(mDstepInfo.getScriptCurrent());
                            ruleConfCurrent.setHashcode(mDstepInfo.getScriptCurrentHash());
                            ruleConfCurrent.setRuleGroup(1L);
                            ruleConfCurrent.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"current");
                            ruleConfCurrent.setStepName(mDstepInfo.getStepName());
                            ruleConfigCurrlist.add(ruleConfCurrent);
                            cacheScript(ruleConfCurrent.getStepSign(), ruleConfCurrent.getRuleScript(), ruleConfCurrent.getHashcode());
                            ruleIds.add(ruleConfCurrent.getStepSign());
                        }
                        if (mDstepInfo.getScriptTemperature() != null) {
                            RuleConfig ruleConfTemperature = new RuleConfig();
                            ruleConfTemperature.setRuleScript(mDstepInfo.getScriptTemperature());
                            ruleConfTemperature.setHashcode(mDstepInfo.getScriptTemperatureHash());
                            ruleConfTemperature.setRuleGroup(1L);
                            ruleConfTemperature.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"temp");
                            ruleConfTemperature.setStepName(mDstepInfo.getStepName());
                            ruleConfigTemplist.add(ruleConfTemperature);
                            cacheScript(ruleConfTemperature.getStepSign(), ruleConfTemperature.getRuleScript(), ruleConfTemperature.getHashcode());
                            ruleIds.add(ruleConfTemperature.getStepSign());
                        }
                        if (mDstepInfo.getScriptTime() != null) {
                            RuleConfig ruleConfTime = new RuleConfig();
                            ruleConfTime.setRuleScript(mDstepInfo.getScriptTime());
                            ruleConfTime.setHashcode(mDstepInfo.getScriptTimeHash());
                            ruleConfTime.setRuleGroup(1L);
                            ruleConfTime.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"time");
                            ruleConfTime.setStepName(mDstepInfo.getStepName());
                            ruleConfigTimelist.add(ruleConfTime);
                            cacheScript(ruleConfTime.getStepSign(), ruleConfTime.getRuleScript(), ruleConfTime.getHashcode());
                            ruleIds.add(ruleConfTime.getStepSign());
                        }
                        if (mDstepInfo.getScriptVoltage() != null) {
                            RuleConfig ruleConfVoltage = new RuleConfig();
                            ruleConfVoltage.setRuleScript(mDstepInfo.getScriptVoltage());
                            ruleConfVoltage.setHashcode(mDstepInfo.getScriptVoltageHash());
                            ruleConfVoltage.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"volt");
                            ruleConfVoltage.setStepName(mDstepInfo.getStepName());
                            ruleConfVoltage.setRuleGroup(1L);
                            ruleConfigVoltlist.add(ruleConfVoltage);
                            cacheScript(ruleConfVoltage.getStepSign(), ruleConfVoltage.getRuleScript(), ruleConfVoltage.getHashcode());
                            ruleIds.add(ruleConfVoltage.getStepSign());
                        }
                        if (mDstepInfo.getScriptEnergy() != null && mDstepInfo.getScriptEnergy().equals("")) {
                            RuleConfig ruleConfEnergy = new RuleConfig();
                            ruleConfEnergy.setRuleScript(mDstepInfo.getScriptEnergy());
                            ruleConfEnergy.setHashcode(mDstepInfo.getScriptEnergyHash());
                            ruleConfEnergy.setRuleGroup(1L);
                            ruleConfEnergy.setStepSign(mDstepInfo.getRemark() + String.valueOf(mDstepInfo.getStepId())+"energy");
                            ruleConfEnergy.setStepName(mDstepInfo.getStepName());
                            ruleConfigEnerlist.add(ruleConfEnergy);
                            cacheScript(ruleConfEnergy.getStepSign(), ruleConfEnergy.getRuleScript(), ruleConfEnergy.getHashcode());
                            ruleIds.add(ruleConfEnergy.getStepSign());
                        }
                        *//***将每个流程的各种场景放入scriptcachemapping*//*
                    }
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_curr", ruleConfigCurrlist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_capa", ruleConfigCapalist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_enger", ruleConfigEnerlist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_temp", ruleConfigTemplist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_time", ruleConfigTimelist);
                    ruleConfig.put(mDprocessInfo.getProcessId()+"_volt", ruleConfigVoltlist);
                    System.out.println("ruleConfig长度："+ruleConfig.size());
                    System.out.println("ruleIds长度："+ruleIds.size());
                }
            }
            processInfoJson = newMdProcessjosn ;*/

            //以下是测试
            /*for (Map.Entry<String, List<RuleConfig>> entries : ruleConfig.entrySet()){
                System.out.println("MapruleConfig的key是"+entries.getKey());
                for (RuleConfig ruleConfig : entries.getValue()){
                    System.out.println(ruleConfig.getStepSign());
                    System.out.println(ruleConfig.getRuleScript());
                }
            }*/
            //System.out.println(processInfoJson.length());
        } finally {
            JedisFactory.closeQuietly(jedis);
        }
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
                    System.out.println("有删除cachedIds吗");
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
    private boolean compareSet(Set<String> str1,Set<String> str2){

        if (str1.size() != str2.size()){
            flag = true;
        }else {
            for (String str : str1){
                if (!str2.contains(str)){
                    flag = true;
                }
            }
        }
        return flag;
    }
}