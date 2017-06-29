package com.edcs.tds.storm.service;

import java.io.UnsupportedEncodingException;
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
import com.edcs.tds.storm.model.VariableDict;
import com.edcs.tds.storm.util.DataSerializer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
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

	private static ConcurrentMap<Long, List<RuleConfig>> ruleConfig = Maps.newConcurrentMap();

	private static Vector<Long> ruleIds = new Vector<Long>();

	private static ScriptCacheMapping scriptCacheMapping = new ScriptCacheMapping();

	private static volatile boolean inited = false;

	private static volatile Thread updateThread;

	private String ruleConfigVersion;

	public void setProxyJedisPool(ProxyJedisPool proxyJedisPool) {
		this.proxyJedisPool = proxyJedisPool;
	}

	public static VariableDict getVariableDict() {
		return variableDict.get();
	}

	public static ConcurrentMap<Long, Pair<String, Script>> getScriptCache() {
		return scriptCacheMapping.getScriptCache();
	}

	public static ConcurrentMap<Long, List<RuleConfig>> getRuleConfig() {
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
			ruleConfigVersion = newVersion;
		} finally {
			JedisFactory.closeQuietly(jedis);
		}
	}

	/**
	 * 清理无效的脚本缓存
	 */
	public void cleanInvalidCache() {
		if (ruleIds.size() > 0 && scriptCacheMapping.getScriptCache().size() > 0) {
			Set<Long> cachedIds = scriptCacheMapping.getScriptCache().keySet();
			for (Long id : cachedIds) {
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
	private void cacheScript(Long id, String scriptContent, String hashcode) {
		// 判断新的脚本内容与缓存是否有差异，如果有则重新编译
		if (!scriptCacheMapping.isDifference(id, hashcode)) {
			scriptCacheMapping.addScript(id, hashcode, ScriptExecutor.getDefaultShell().parse(scriptContent));
			logger.info("Finding a new script, recompile and cache, rule id: {}.", id);
		}
	}

}