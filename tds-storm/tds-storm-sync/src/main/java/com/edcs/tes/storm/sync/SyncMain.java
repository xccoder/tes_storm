package com.edcs.tes.storm.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tes.storm.util.SpringBeanFactory;


public class SyncMain {
    private static final Logger logger = LoggerFactory.getLogger(SyncMain.class);

    public static void main(String[] args) {
        int threadNum = 10;
        if (args != null && args.length > 0) {
            threadNum = Integer.parseInt(args[0]);
        }
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(threadNum); //线程池
        SpringBeanFactory beanFactory = new SpringBeanFactory();
        DataSyncService threadTask = new DataSyncService(beanFactory);//线程任务
        while (true) {
            cachedThreadPool.execute(threadTask);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                logger.error("", e);
            }
        }
    }
}
