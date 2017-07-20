package com.edcs.tds.storm.service;

import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class MessageRepeatFilter {
    private static final Logger logger = LoggerFactory.getLogger(MessageRepeatFilter.class);
    private int expireTime = 60 * 60 * 8;
    private JedisPool jedisPool;
    private static Joiner joiner = Joiner.on(":").skipNulls();

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public boolean filter(Object... keys) {
        boolean flag = false;
        String key = joiner.join(keys);
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

