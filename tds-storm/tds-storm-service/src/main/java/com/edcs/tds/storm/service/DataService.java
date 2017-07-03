package com.edcs.tds.storm.service;

import com.edcs.tds.storm.model.TestingMessage;

public class DataService {

	public int getCurrentCycleCount(String remark) {
		// 如果有值则返回，无则返回-1
		// TODO 查询Redis的循环次数
		
		return -1;
	}
    /**
     * 修改测试数据中的业务循环号（businessCycle）
     * @param testingMessage
     */
	public TestingMessage updateBusinessCycle(TestingMessage testingMessage) {
		/*
		 * 1.首先判断这条测试数据是否是某个测试流程数据中的第一条数据。
		 * 如果是第一条数据，则需要判断这条数据对应的流程对应的rootRemark 有没有值
		 * 如果有则需要查询 rootRemark 对应的流程 的循环次数
		 * 将上一个的循环次数+1 作为当前循环的起始循环号。
		 * 2.查询这条测试数据对应的公布的 IS_CYCLE_SIGNAL_STEP 如果为true 则
		 * 根据流程号+测试编号来查询这条测试数据的上一条测试数据的公布类型，如果两条数据的公布类型不同，则业务循环号需要+1
		 * 反之则不需要+1.  如果 IS_CYCLE_SIGNAL_STEP 为false 则直接跳过，业务循环号不需要+1.
		 */
		
		if(testingMessage==null){
			return null;
		}
		if(testingMessage.getSequenceId()==1){//表明这个测试数据是某个流程的起始数据（第一条数据）
			
		}
		
		
		return testingMessage;
	}

}
