package com.edcs.tes.storm.sync;

import java.util.ArrayList;
import java.util.List;

import com.edcs.tes.storm.util.SpringBeanFactory;


public class SyncMain {
	
	public static void main(String[] args) {
		int threadNum = 5;
		if(args!=null && args.length>0){
			threadNum = Integer.parseInt(args[0]);
		}
		SpringBeanFactory beanFactory = new SpringBeanFactory();
		DataSyncService threadTask = new DataSyncService(beanFactory);//线程任务
		List<Thread> threads = new ArrayList<Thread>();
		for(int i = 0;i<threadNum;i++){
			Thread thread = new Thread(threadTask);
			threads.add(thread);
		}
		for (Thread thread : threads) {
			thread.start();
			thread.run();
		}
	}
}
