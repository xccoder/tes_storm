package com.edcs.tds.storm.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;
import com.edcs.tds.storm.model.TestingMessage;

import redis.clients.jedis.Jedis;

public class DataService {

	public int getCurrentCycleCount(String remark,CacheService cacheService) {
		//查询某个流程的Redis的循环次数  如果有值则返回，无则返回-1
		Jedis jedis = cacheService.getProxyJedisPool().getResource();
		String cycleNum = jedis.get(remark);
		if(!StringUtils.isNotBlank(cycleNum)){
			return -1;
		}
		return Integer.parseInt(cycleNum);
	}
	
    /**
     * 维护测试数据中的业务循环号（businessCycle）
     * 
     * 1.首先判断这条测试数据是否是某个测试流程数据中的第一条数据。
	 * 如果是第一条数据，则需要判断这条数据对应的流程对应的rootRemark 有没有值
	 * 如果有则需要查询 rootRemark 对应的流程 的循环次数
	 * 将上一个的循环次数+1 作为当前循环的起始循环号。
	 * 2.查询这条测试数据对应的公布的 IS_CYCLE_SIGNAL_STEP 如果为true 则
	 * 根据流程号+测试编号来查询这条测试数据的上一条测试数据的公布类型，如果两条数据的公布类型不同，则业务循环号需要+1
	 * 反之则不需要+1.  如果 IS_CYCLE_SIGNAL_STEP 为false 则直接跳过，业务循环号不需要+1.
     * @param testingMessage
     */
	public TestingMessage updateBusinessCycle(TestingMessage testingMessage,CacheService cacheService) {
		if(testingMessage==null || cacheService==null){
			return null;
		}
		/*
		 * 到CacheService中获取这个测试数据对应的流程信息
		 * （也可以从redis里面获取，但是考虑到性能问题，就让CacheService中缓存一份redis所有流程的信息。这样就是牺牲内存换性能）
		 */
		List<MDprocessInfo> mDprocessInfos = JsonUtils.toArray(cacheService.getProcessInfoJson(), MDprocessInfo.class);
		if(testingMessage.getSequenceId()==1){//表明这个测试数据是某个流程的起始数据（第一条数据）
			for (MDprocessInfo mDprocessInfo : mDprocessInfos) {
				if(testingMessage.getRemark()!=null && testingMessage.getRemark().equals(mDprocessInfo.getRemark())){
					String rootRemark = mDprocessInfo.getRootRemark();//获取该条测试数据对应流程的rootremark的值
					if(StringUtils.isNotBlank(rootRemark)){//如果rootRemark有值
						/*
						 * 根据rootRemark先去redis中查询这条和rootRemark对应的remark的流程数据，如果找不到则再去HANA中找。
						 * （查找这个和rootRemark相同的remark 的流程循环次数）
						 */
						int cycleNum = GetDataInterface.getCycleNum(rootRemark,cacheService);
						testingMessage.setBusinessCycle(cycleNum+1);
						break;
					}else{
						testingMessage.setBusinessCycle(1);
						break;
					}
				}
			}
		}else{//如果不等于1
			//查看这条测试数据对应的工步的IS_CYCLE_SIGNAL_STEP 是否为true，如果为true则进行下面的操作，如果为false，则什么都不做
			bre:for (MDprocessInfo mDprocessInfo : mDprocessInfos) {
				if(testingMessage.getRemark()!=null && testingMessage.getRemark().equals(mDprocessInfo.getRemark())){//找到这个测试数据对应的流程
					List<MDStepInfo> mdStepInfoList = mDprocessInfo.getMdStepInfoList();//获取这个流程对应的所有的工步信息。
					for (MDStepInfo mdStepInfo : mdStepInfoList) {
						if(testingMessage.getStepId()==mdStepInfo.getStepId()){//找到这个测试信息对应的工步信息
							boolean isCycleSignalStep = mdStepInfo.isCycleSignalStep();
							if(isCycleSignalStep){
								//获取这条测试数据的上一条测试数据（通过流程的remark+记录序号来获取）
								TestingMessage  upTestingMessage = GetDataInterface.getUpTestingMsg(testingMessage,1,cacheService);
								if(upTestingMessage==null){
//									throw new Exception("该数据的上一条数据找不到，可能上一条数据还没有处理完！！");
									System.out.println("该数据的上一条数据找不到，可能上一条数据还没有处理完！！");
								}
								if(upTestingMessage.getStepId()!=testingMessage.getStepId()){
									//如果当前测试数据的工步类型不等于它上一条数据的工步类型则businessCycle+1
									testingMessage.setBusinessCycle(testingMessage.getBusinessCycle()+1);
//									break;
									break bre;
								}
							}
						}
					}
				}
			}
		}
		return testingMessage;
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
