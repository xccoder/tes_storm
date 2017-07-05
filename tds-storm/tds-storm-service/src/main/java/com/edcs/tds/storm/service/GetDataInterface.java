package com.edcs.tds.storm.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;

import redis.clients.jedis.Jedis;

/**
 * 获取数据的接口
 * @author LiQF
 *
 */
public class GetDataInterface {
	DBHelperUtils dbUtils = new DBHelperUtils();
	/**
	 * 根据流程号和序号来获取一条测试数据的上一条数据信息
	 * 先到redis中去找，如果找不到再去HANA中找
	 * @param testingMessage
	 * @param i 表示获取这条测试数据的上几条测试数据
	 * @return
	 */
	public static TestingMessage getUpTestingMsg(TestingMessage testingMessage,int i,CacheService cacheService){
		TestingMessage testingMsg = new TestingMessage();//用于返回值
		ProxyJedisPool jedisPool = cacheService.getProxyJedisPool();//获取连接池
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(testingMessage.getRemark()+(testingMessage.getSequenceId()-i));
		if(StringUtils.isNotBlank(json)){
			List<TestingResultData> testingResultData = JsonUtils.toArray(json, TestingResultData.class);
			for (TestingResultData testingResultData2 : testingResultData) {
//				testingMsg.setSfc(testingResultData2.getSfc());//电芯号。
//				testingMsg.setRemark(testingResultData2.getRemark());
//				testingMsg.setChannelId(testingResultData2.getChannelId());//通道号
//			    testingMsg.setSequenceId(testingResultData2.getSequenceId());//记录序号
//				testingMsg.setCycle(testingResultData2.getCycle());
//				testingMsg.setStepId(testingResultData2.getStepId());//工步序号
//				testingMsg.setStepName(testingResultData2.getStepName());//公布名称
//				testingMsg.setTestTimeDuration(testingResultData2.getTestTimeDuration());//测试相对时常
				      
			}
			
		}else{//到hana中查询
			
		}
		return null;
	}
	
	/**
	 * 根据rootRemark先去redis中查询这条和rootRemark对应的remark的流程数据，如果找不到则再去HANA中找。
	 * （查找这个和rootRemark相同的remark 的流程循环次数）
	 * @param remark
	 * @return
	 */
	 public static int  getCycleNum(String remark,CacheService cacheService){
		 int cycleNum = 0;
		 
		 return cycleNum;
	 }
	
	 
	 public static void main(String[] args) {
			System.out.println("12222222");
			String sql = "select *from TX_ALERT_INFO";// SQL语句
			DBHelperUtils db1 = new DBHelperUtils();// 创建DBHelper对象
			try {
				ResultSet ret = db1.selectList(sql);// 执行语句，得到结果集
				while (ret.next()) {
					String uid = ret.getString(1);
					String ufname = ret.getString(2);
					String ulname = ret.getString(3);
					String udate = ret.getString(4);
					System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate);
				} // 显示数据
				ret.close();
				db1.close();// 关闭连接
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}  

}
