package com.edcs.tes.storm.sync;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.IResultData;
import com.edcs.tes.storm.dao.impl.ResultDataImpl;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by CaiSL2 on 2017/7/4.
 * 数据同步服务
 * 将存储在redis的所有结果数据，多线程的将其读出并放入到hana数据库中
 *
 */

public class DataSyncService implements Runnable {

    private RedisSync redisSync;
    private IResultData iResultData = new ResultDataImpl();
    @Override
    public void run() {
        String processJson = redisSync.getProcessJson();
            List<TestingResultData> testingResultDatas = JsonUtils.toArray(processJson,TestingResultData.class);
        try {
            iResultData.insertResultData(testingResultDatas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
