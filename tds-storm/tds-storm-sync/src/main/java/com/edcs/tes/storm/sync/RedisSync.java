package com.edcs.tes.storm.sync;
import com.edcs.tds.common.redis.ProxyJedisPool;
import redis.clients.jedis.Jedis;

/**
 * Created by CaiSL2 on 2017/7/4.
 *
 */
public class RedisSync {

    private ProxyJedisPool proxyJedisPool;

    public ProxyJedisPool getProxyJedisPool() {
        return proxyJedisPool;
    }

    public void setProxyJedisPool(ProxyJedisPool proxyJedisPool) {
        this.proxyJedisPool = proxyJedisPool;
    }

    public RedisSync(ProxyJedisPool proxyJedisPool) {
        this.proxyJedisPool = proxyJedisPool;
    }
    public  String getProcessJson(){
        Jedis jedis = null;
        String resultJson = null;
        try {
            jedis = proxyJedisPool.getResource();
            resultJson = jedis.spop("TES-RESULT");
        }catch (Exception e){
           ///
        }
        return resultJson;
    }
}
