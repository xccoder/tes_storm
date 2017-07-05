package com.edcs.tds.storm.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
 * 
 * @author LiQF
 *
 */
public class GetDataInterface {
	// DBHelperUtils dbUtils = new DBHelperUtils();
	/**
	 * 根据流程号和序号来获取一条测试数据的上一条数据信息 先到redis中去找，如果找不到再去HANA中找
	 * 
	 * @param testingMessage
	 * @param i
	 *            表示获取这条测试数据的上几条测试数据
	 * @return
	 */
	public static TestingMessage getUpTestingMsg(TestingMessage testingMessage, int i, CacheService cacheService) {
		TestingMessage testingMsg = new TestingMessage();// 用于返回值
		// 先从redis读取数据，如果读取不到，则再去hana中读取
		ProxyJedisPool jedisPool = cacheService.getProxyJedisPool();// 获取连接池
		Jedis jedis = jedisPool.getResource();
		String json = jedis.get(testingMessage.getRemark() + (testingMessage.getSequenceId() - i));
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
					+ "from tx_original_process_data where REMARK= '" + testingMessage.getRemark()
					+ "' and SEQUENCE_ID=" + (testingMessage.getSequenceId() - i);
			DBHelperUtils dbUtils = cacheService.getDbUtils();
			Connection conn = dbUtils.getConnection();
			PreparedStatement pst = null;
			ResultSet results = null;
			try {
				pst = conn.prepareStatement(sql);
				results = pst.executeQuery();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (results != null) {
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
						testingMsg.setTimestamp(results.getDate(10));
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
				e.printStackTrace();
				dbUtils.close(conn, pst, results);
			} finally {
				dbUtils.close(conn, pst, results);
			}
		}
		return testingMsg;
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
		String sql = "select total_cycle_num from MD_PROCESS_INFO where remark = " + rootRemark;
		DBHelperUtils dbUtils = cacheService.getDbUtils();
		Connection conn = dbUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet results = null;
		try {
			pst = conn.prepareStatement(sql);
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

	public static void main(
			String[] args) {/*
							 * System.out.println("12222222"); String sql =
							 * "select *from TX_ALERT_INFO";// SQL语句
							 * DBHelperUtils db1 = new DBHelperUtils();//
							 * 创建DBHelper对象 try { ResultSet ret =
							 * db1.selectList(sql);// 执行语句，得到结果集 while
							 * (ret.next()) { String uid = ret.getString(1);
							 * String ufname = ret.getString(2); String ulname =
							 * ret.getString(3); String udate =
							 * ret.getString(4); System.out.println(uid + "\t" +
							 * ufname + "\t" + ulname + "\t" + udate); } // 显示数据
							 * ret.close(); db1.close();// 关闭连接 } catch
							 * (SQLException e) { e.printStackTrace(); }
							 */
		TestingMessage testingMsg = new TestingMessage();// 用于返回值
		testingMsg.setRemark("T3-20170215-1892-557478_CL_61715601026");
		testingMsg.setSequenceId(16);
		TestingMessage testingMsg11 = getUpTestingMsg(testingMsg, 1, null);
		System.out.println(testingMsg11);
	}

}
