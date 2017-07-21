package com.edcs.tds.storm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.redis.ProxyJedisPool;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;

import redis.clients.jedis.Jedis;

/**
 * 获取数据的接口
 * 
 * @author LiQF
 *
 */
public class GetDataInterface {
	private static final Logger logger = LoggerFactory.getLogger(GetDataInterface.class);
	/**
	 * 根据流程号和序号来获取一条测试数据的上一条数据信息 先到redis中去找，如果找不到再去HANA中找
	 * 
	 * @param testingMessage
	 * @param i
	 *            表示获取这条测试数据的上几条测试数据
	 * @return
	 */
	public static TestingMessage getUpTestingMsg(TestingMessage testingMessage, int i, CacheService cacheService) {
		TestingMessage testingMsg = null;// 用于返回值
		// 先从redis读取数据，如果读取不到，则再去hana中读取
		ProxyJedisPool jedisPool = cacheService.getProxyJedisPool();// 获取连接池
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(testingMessage.getRemark() +"_"+(testingMessage.getSequenceId() - i));
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
		if (StringUtils.isNotBlank(json)) {
			List<TestingResultData> testingResultData = JsonUtils.toArray(json, TestingResultData.class);
			for (TestingResultData testingResultData2 : testingResultData) {
				testingMsg = testingResultData2.getTestingMessage();
				break;
			}

		} else {// 如果读取不到，则再去hana中读取
			String sql = "select REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,"
					+ "CYCLE,STEP_ID,STEP_NAME,TEST_TIME_DURATION,TIMESTAMP,SV_IC_RANGE,"
					+ "SV_IV_RANGE,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,"
					+ "PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,ST_BUSINESS_CYCLE "
					+ "from tx_original_process_data where REMARK= ? and SEQUENCE_ID = ?";
			DBHelperUtils dbUtils = cacheService.getDbUtils();
			Connection conn = dbUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet results = null;
			try {
				pst = conn.prepareStatement(sql);
				pst.setString(1, testingMessage.getRemark());
				pst.setInt(2, testingMessage.getSequenceId() - i);
				results = pst.executeQuery();
				if (results != null) {
					testingMsg = new TestingMessage();
					while (results.next()) {
						testingMsg.setRemark(results.getString(1));
						testingMsg.setSfc(results.getString(2));
						testingMsg.setResourceId(results.getString(3));
						testingMsg.setChannelId(results.getInt(4));
						testingMsg.setSequenceId(results.getInt(5));
						testingMsg.setCycle(results.getInt(6));
						testingMsg.setStepId(results.getInt(7));
						testingMsg.setStepName(results.getString(8));
						testingMsg.setTestTimeDuration(results.getBigDecimal(9));
						testingMsg.setTimestamp(results.getTimestamp(10));
						testingMsg.setSvIcRange(results.getBigDecimal(11));
						testingMsg.setSvIvRange(results.getBigDecimal(12));
						testingMsg.setPvVoltage(results.getBigDecimal(13));
						testingMsg.setPvCurrent(results.getBigDecimal(14));
						testingMsg.setPvIr(results.getBigDecimal(15));
						testingMsg.setPvTemperature(results.getBigDecimal(16));
						testingMsg.setPvChargeCapacity(results.getBigDecimal(17));
						testingMsg.setPvDischargeCapacity(results.getBigDecimal(18));
						testingMsg.setPvChargeEnergy(results.getBigDecimal(19));
						testingMsg.setPvDischargeEnergy(results.getBigDecimal(20));
						testingMsg.setBusinessCycle(results.getInt(21));
					}

				}
			} catch (Exception e) {
				logger.error("执行hana数据库连接出现错误，错误位置为 GetDataInterface.getUpTestingMsg方法",e);
				dbUtils.close(conn, pst, results);
			} finally {
				dbUtils.close(conn, pst, results);
			}
		}
		return testingMsg;
	}
	/**
	 * 获取一个循环中一个工步中的第一条数据
	 * @param testingMessage
	 * @param cacheService
	 * @return
	 */
	public static TestingMessage getFirstTestingMsg(TestingMessage testingMessage,CacheService cacheService) {
		TestingMessage testingMsgResult = null;// 用于返回值
		// 先从redis读取数据，如果读取不到，则再去hana中读取
		ProxyJedisPool jedisPool = cacheService.getProxyJedisPool();// 获取连接池
		Jedis jedis = jedisPool.getResource();
		Set<String> keys = jedis.keys(testingMessage.getRemark()+"_*");
		for (String string : keys) {
			String json = jedis.get(string);
			List<TestingResultData> testingResultDatas = JsonUtils.toArray(json, TestingResultData.class);
			TestingMessage testingMsg = testingResultDatas.get(0).getTestingMessage();
			int businessCycle = testingMsg.getBusinessCycle();//业务循环号
			//如果这条测试数据是当前测试数据的同一个循环的同一个工步的第一条测试数据，则取出。
			if(businessCycle == testingMessage.getBusinessCycle() && testingMsg.getStepId()==testingMessage.getStepId() &&testingMsg.getPvDataFlag() == 89 ){
				testingMsgResult = testingMsg;
				break;
			}
		}
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
		if (testingMsgResult==null){// 如果读取不到，则再去hana中读取
			testingMsgResult = new TestingMessage();
			String sql = "select REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,"
					+ "CYCLE,STEP_ID,STEP_NAME,TEST_TIME_DURATION,TIMESTAMP,SV_IC_RANGE,"
					+ "SV_IV_RANGE,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,"
					+ "PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,ST_BUSINESS_CYCLE "
					+ "from tx_original_process_data where REMARK= ? and ST_BUSINESS_CYCLE = ? and STEP_ID = ? and PV_DATA_FLAG = 89";
			DBHelperUtils dbUtils = cacheService.getDbUtils();
			Connection conn = dbUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet results = null;
			try {
				pst = conn.prepareStatement(sql);
				pst.setString(1, testingMessage.getRemark());
				pst.setInt(2, testingMessage.getBusinessCycle());
				pst.setInt(3, testingMessage.getStepId());
				results = pst.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (results != null) {
					while (results.next()) {
						testingMsgResult.setRemark(results.getString(1));
						testingMsgResult.setSfc(results.getString(2));
						testingMsgResult.setResourceId(results.getString(3));
						testingMsgResult.setChannelId(results.getInt(4));
						testingMsgResult.setSequenceId(results.getInt(5));
						testingMsgResult.setCycle(results.getInt(6));
						testingMsgResult.setStepId(results.getInt(7));
						testingMsgResult.setStepName(results.getString(8));
						testingMsgResult.setTestTimeDuration(results.getBigDecimal(9));
						testingMsgResult.setTimestamp(results.getTimestamp(10));
						testingMsgResult.setSvIcRange(results.getBigDecimal(11));
						testingMsgResult.setSvIvRange(results.getBigDecimal(12));
						testingMsgResult.setPvVoltage(results.getBigDecimal(13));
						testingMsgResult.setPvCurrent(results.getBigDecimal(14));
						testingMsgResult.setPvIr(results.getBigDecimal(15));
						testingMsgResult.setPvTemperature(results.getBigDecimal(16));
						testingMsgResult.setPvChargeCapacity(results.getBigDecimal(17));
						testingMsgResult.setPvDischargeCapacity(results.getBigDecimal(18));
						testingMsgResult.setPvChargeEnergy(results.getBigDecimal(19));
						testingMsgResult.setPvDischargeEnergy(results.getBigDecimal(20));
						testingMsgResult.setBusinessCycle(results.getInt(21));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				dbUtils.close(conn, pst, results);
			} finally {
				dbUtils.close(conn, pst, results);
			}
		}
		return testingMsgResult;
	}
	
	
	/**
	 * 获取一个流程中的上一个工步的最后一条数据（先到redis中获取，如果获取不到就到hana中获取）
	 * @param testingMessage
	 * @param i（表明上几个工步）
	 * @param cacheService
	 * @return
	 */
	public static TestingMessage getUpStepTestingMsg(TestingMessage testingMessage, int i, CacheService cacheService){
		TestingMessage testingMsgReturn = null;
		ProxyJedisPool jedisPool = cacheService.getProxyJedisPool();// 获取连接池
		Jedis jedis = jedisPool.getResource();
		Set<String> keys = jedis.keys(testingMessage.getRemark()+"_*");
		for (String string : keys) {
			String json = jedis.get(string);
			List<TestingResultData> testingResultDatas = JsonUtils.toArray(json, TestingResultData.class);
			TestingMessage testingMsg = testingResultDatas.get(0).getTestingMessage();
			int stepLogicNumber = testingMsg.getStepLogicNumber();
			//如果这条测试数据是当前测试数据的上i个工步中的测试数据，且是上i个工步中的最后一条数据，则取出。
			if(stepLogicNumber == testingMessage.getStepLogicNumber()-i && testingMsg.getPvDataFlag() == 88 ){
				testingMsgReturn = testingMsg;
				break;
			}
		}
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
		if(testingMsgReturn == null){//redis 中没有找到，则到hana中找
			String sql = "select REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,"
					+ "CYCLE,STEP_ID,STEP_NAME,TEST_TIME_DURATION,TIMESTAMP,SV_IC_RANGE,"
					+ "SV_IV_RANGE,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,"
					+ "PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,ST_BUSINESS_CYCLE "
					+ "from tx_original_process_data where REMARK= ? and step_logic_number = ? and PV_DATA_FLAG = 88";
			DBHelperUtils dbUtils = cacheService.getDbUtils();
			Connection conn = dbUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet results = null;
			try {
				pst = conn.prepareStatement(sql);
				pst.setString(1, testingMessage.getRemark());
				pst.setInt(2, testingMessage.getStepLogicNumber() - i);
				results = pst.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (results != null) {
					while (results.next()) {
						testingMsgReturn = new TestingMessage();
						testingMsgReturn.setRemark(results.getString(1));
						testingMsgReturn.setSfc(results.getString(2));
						testingMsgReturn.setResourceId(results.getString(3));
						testingMsgReturn.setChannelId(results.getInt(4));
						testingMsgReturn.setSequenceId(results.getInt(5));
						testingMsgReturn.setCycle(results.getInt(6));
						testingMsgReturn.setStepId(results.getInt(7));
						testingMsgReturn.setStepName(results.getString(8));
						testingMsgReturn.setTestTimeDuration(results.getBigDecimal(9));
						testingMsgReturn.setTimestamp(results.getTimestamp(10));
						testingMsgReturn.setSvIcRange(results.getBigDecimal(11));
						testingMsgReturn.setSvIvRange(results.getBigDecimal(12));
						testingMsgReturn.setPvVoltage(results.getBigDecimal(13));
						testingMsgReturn.setPvCurrent(results.getBigDecimal(14));
						testingMsgReturn.setPvIr(results.getBigDecimal(15));
						testingMsgReturn.setPvTemperature(results.getBigDecimal(16));
						testingMsgReturn.setPvChargeCapacity(results.getBigDecimal(17));
						testingMsgReturn.setPvDischargeCapacity(results.getBigDecimal(18));
						testingMsgReturn.setPvChargeEnergy(results.getBigDecimal(19));
						testingMsgReturn.setPvDischargeEnergy(results.getBigDecimal(20));
						testingMsgReturn.setBusinessCycle(results.getInt(21));
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				dbUtils.close(conn, pst, results);
			} finally {
				dbUtils.close(conn, pst, results);
			}
			
		}
		return testingMsgReturn; 
	}
	

	/**
	 * 根据rootRemark先去redis中查询这条和rootRemark对应的remark的流程数据，如果找不到则再去HANA中找。
	 * （查找这个和rootRemark相同的remark 的流程循环次数）
	 * 
	 * @param remark
	 * @return
	 */
	public static int getCycleNum(String rootRemark, CacheService cacheService) {
		int cycleNum = 0;
		String sql = "select total_cycle_num from MD_PROCESS_INFO where remark = ?";
		DBHelperUtils dbUtils = cacheService.getDbUtils();
		Connection conn = dbUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet results = null;
		try {
			pst = conn.prepareStatement(sql);
			pst.setString(1, rootRemark);
			results = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (results != null) {
				while (results.next()) {
					cycleNum = results.getInt(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			dbUtils.close(conn, pst, results);
		} finally {
			dbUtils.close(conn, pst, results);
		}
		return cycleNum;
	}

	public static void main(String[] args) throws Exception{
		 /*System.out.println("12222222"); 
		 String sql ="select *from TX_ALERT_INFO";// SQL语句
		  DBHelperUtils db1 = new DBHelperUtils();//创建DBHelper对象 
		  ComboPooledDataSource pool = new ComboPooledDataSource();
		  pool.setDriverClass("com.sap.db.jdbc.Driver");
		  pool.setJdbcUrl("jdbc:sap://172.26.66.36:30015/");
		  pool.setUser("TES");
		  pool.setPassword("Aa123456");
		  db1.setDataSource(pool);
		  try { 
			  Connection conn = db1.getConnection();
			  
			  CallableStatement c = conn.prepareCall("{call \"TES\".\"EXTRACTIONDATA\"(?, ?, ?, ?)}");
			    c.setString(1, "T3-20170215-1892-557478_CL_61715601026");
			    c.setInt(2, 0);	
			    c.setInt(3, 1);
			    c.registerOutParameter(4, java.sql.Types.VARCHAR);
			    c.execute();
			    System.out.println(c.getString(4));
       System.out.println("end");
			  
//			  PreparedStatement pst = conn.prepareStatement(sql);
//			  ResultSet ret = pst.executeQuery();
////						      ResultSet ret = db1.selectList(sql);// 执行语句，得到结果集 
//		  while(ret.next()) { String uid = ret.getString(1);
//		  String ufname = ret.getString(2); 
//		  String ulname =ret.getString(3); 
//		  String udate =ret.getString(4); 
//		  System.out.println(uid + "\t" + ufname + "\t" + ulname + "\t" + udate); } // 显示数据
//		  ret.close(); 
		  } catch(SQLException e) { e.printStackTrace(); }
							 
//		TestingMessage testingMsg = new TestingMessage();// 用于返回值
//		testingMsg.setRemark("T3-20170215-1892-557478_CL_61715601026");
//		testingMsg.setSequenceId(16);
//		TestingMessage testingMsg11 = getUpTestingMsg(testingMsg, 1, null);
//		System.out.println(testingMsg11);
*/	}
}
