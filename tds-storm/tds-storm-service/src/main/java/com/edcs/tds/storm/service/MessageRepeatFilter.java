package com.edcs.tds.storm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.redis.ProxyJedisPool;

import redis.clients.jedis.Jedis;

public class MessageRepeatFilter {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepeatFilter.class);
    private int expireTime = 60 * 60 * 8;
    private ProxyJedisPool jedisPool;
//    private static Joiner joiner = Joiner.on(":").skipNulls();

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public void setJedisPool(ProxyJedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public boolean filter(String  key) {
        boolean flag = false;
//        String key = joiner.join(keys);
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (jedis != null) {
                long count = jedis.setnx(key, String.valueOf(System.currentTimeMillis()));
                if (count > 0) {
                    jedis.expire(key, expireTime);
                    flag = true;
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        } finally {
            if (jedis != null)
                jedis.close();
        }
        return flag;
    }
}

