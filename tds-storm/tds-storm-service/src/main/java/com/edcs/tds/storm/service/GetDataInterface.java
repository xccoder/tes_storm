package com.edcs.tds.storm.service;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.storm.model.TestingMessage;

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
