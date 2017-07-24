package com.edcs.tes.storm.sync;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.impl.ResultDataImpl;
import com.edcs.tes.storm.util.SpringBeanFactory;

import redis.clients.jedis.Jedis;

/**
 * Created by CaiSL2 on 2017/7/4.
 * 数据同步服务
 * 将存储在redis的所有结果数据，多线程的将其读出并放入到hana数据库中
 */

public class DataSyncService implements Runnable {
     
	private final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
	private ProxyJedisPool proxyJedisPool;
	private DBHelperUtils dbHelperUtils;
	public DataSyncService(SpringBeanFactory beanFactory){
		this.proxyJedisPool = beanFactory.getBean(ProxyJedisPool.class);
		this.dbHelperUtils = beanFactory.getBean(DBHelperUtils.class);
	}
    @Override
    public void run() {
    	try {
    		String processJson = getProcessJson();
    		if (processJson != null) {
    			List<TestingResultData> testingResultDatas = JsonUtils.toArray(processJson, TestingResultData.class);
    			new ResultDataImpl(dbHelperUtils).insertResultData(testingResultDatas);
    		}
		} catch (Exception e) {
			logger.error(""+e);
		}
    }
    
    public  String getProcessJson(){
        Jedis jedis = null;
        String resultJson = null;
        try {
            jedis = proxyJedisPool.getResource();
            resultJson = jedis.spop("TES-RESULT");//FIXME redis key移到RedisCacheKey中，规范命名
        }catch (Exception e){
            logger.error(""+e);//FIXME 替换为 
        }
        if (jedis != null){
            jedis.close();
        }
        return resultJson;
    }
}
