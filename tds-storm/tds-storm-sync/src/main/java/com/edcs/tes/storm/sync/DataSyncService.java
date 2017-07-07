package com.edcs.tes.storm.sync;

import java.sql.SQLException;
import java.util.List;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.IResultData;
import com.edcs.tes.storm.dao.impl.ResultDataImpl;

/**
 * Created by CaiSL2 on 2017/7/4.
 * 数据同步服务
 * 将存储在redis的所有结果数据，多线程的将其读出并放入到hana数据库中
 *
 */

public class DataSyncService implements Runnable {

    @Override
    public void run() {
    	IResultData iResultData = new ResultDataImpl();
    	RedisSync redisSync = new RedisSync();
        String processJson = redisSync.getProcessJson();
        List<TestingResultData> testingResultDatas = JsonUtils.toArray(processJson,TestingResultData.class);
        try {
            iResultData.insertResultData(testingResultDatas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
