package com.edcs.tds.storm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.redis.JedisFactory;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.redis.RedisCacheKey;

import redis.clients.jedis.Jedis;

public class MessageRepeatFilter {
	private static final Logger logger = LoggerFactory.getLogger(MessageRepeatFilter.class);
	private int expireTime = 60 * 60 * 8;
	private ProxyJedisPool jedisPool;

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public void setJedisPool(ProxyJedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public boolean filter(String key) {
		boolean flag = false;

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			if (jedis != null) {
				long count = jedis.setnx(RedisCacheKey.getFilterKey(key), String.valueOf(System.currentTimeMillis()));
				if (count > 0) {
					jedis.expire(RedisCacheKey.getFilterKey(key), expireTime);
					flag = true;
				}
			}
		} catch (Exception e) {
			logger.error("MessageRepeatFilter.filter 出现异常", e);
		} finally {
			JedisFactory.closeQuietly(jedis);
		}
		return flag;
	}
}
