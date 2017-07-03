package com.edcs.tds.storm.service;

import com.edcs.tds.storm.model.TestingMessage;

/**
 * 获取数据的接口
 * @author LiQF
 *
 */
public class GetDataInterface {
	
	/**
	 * 根据流程号和序号来获取一条测试数据的上一条数据信息
	 * 先到redis中去找，如果找不到再去HANA中找
	 * @param testingMessage
	 * @param i 表示获取这条测试数据的上几条测试数据
	 * @return
	 */
	public static TestingMessage getUpTestingMsg(TestingMessage testingMessage,int i){
		
		return null;
	}
	
	/**
	 * 根据rootRemark先去redis中查询这条和rootRemark对应的remark的流程数据，如果找不到则再去HANA中找。
	 * （查找这个和rootRemark相同的remark 的流程循环次数）
	 * @param remark
	 * @return
	 */
	 public static int  getCycleNum(String remark){
		 int cycleNum = 0;
		 return cycleNum;
	 }
	

}
