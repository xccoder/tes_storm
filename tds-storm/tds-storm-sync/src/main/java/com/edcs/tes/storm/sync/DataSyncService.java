package com.edcs.tes.storm.sync;

import java.sql.SQLException;
import java.util.List;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.IResultData;
import com.edcs.tes.storm.dao.impl.ResultDataImpl;
import com.edcs.tes.storm.util.SpringBeanFactory;

/**
 * Created by CaiSL2 on 2017/7/4.
 * 数据同步服务
 * 将存储在redis的所有结果数据，多线程的将其读出并放入到hana数据库中
 */

public class DataSyncService implements Runnable {
     
	private ProxyJedisPool proxyJedisPool;
	private DBHelperUtils dbHelperUtils;
	public DataSyncService(SpringBeanFactory beanFactory){
		this.proxyJedisPool = beanFactory.getBean(ProxyJedisPool.class);
		this.dbHelperUtils = beanFactory.getBean(DBHelperUtils.class);
	}
    @Override
    public void run() {
        IResultData iResultData = new ResultDataImpl(dbHelperUtils);
        RedisSync redisSync = new RedisSync(proxyJedisPool);
        String processJson = redisSync.getProcessJson();
        if (processJson != null) {
            try {
                List<TestingResultData> testingResultDatas = JsonUtils.toArray(processJson, TestingResultData.class);
                iResultData.insertResultData(testingResultDatas);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
