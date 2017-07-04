package com.edcs.tes.storm.sync.sync;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;

/**
 * Created by CaiSL2 on 2017/7/4.
 * 数据同步服务
 * 将存储在redis的所有结果数据，多线程的将其读出并放入到hana数据库中
 *
 */

public class DataSyncService implements Runnable {

    private RedisSync redisSync;
    private DBHelperUtils db ;
    @Override
    public void run() {
        String processJson = redisSync.getProcessJson();
        db = new DBHelperUtils();
        if (processJson != null){
            TestingResultData testingResultData = JsonUtils.toObject(processJson, TestingResultData.class);
            
        }
    }
}
