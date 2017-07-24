package com.edcs.tds.storm.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;

import redis.clients.jedis.Jedis;

public class DataService {

	public int getCurrentCycleCount(String remark,CacheService cacheService) {
		//查询某个流程的Redis的循环次数  如果有值则返回，无则返回-1
		Jedis jedis = cacheService.getProxyJedisPool().getResource();
		String cycleNum = jedis.get(remark);
		if(!StringUtils.isNotBlank(cycleNum)){
			if(jedis!=null){
            	jedis.close();
            }//FIXME 替换为JedisFactory.closeQuietly(jedis);
			return -1;
		}
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
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
		/*
		 * 到CacheService中获取这个测试数据对应的流程信息
		 * （也可以从redis里面获取，但是考虑到性能问题，就让CacheService中缓存一份redis所有流程的信息。这样就是牺牲内存换性能）
		 */
		Jedis jedis = cacheService.getProxyJedisPool().getResource();
		Set<String> processInfoJsons = cacheService.getProcessInfoJsons();//获取流程主数据 信息
		List<MDprocessInfo> mDprocessInfos = null;
		if(processInfoJsons!=null && processInfoJsons.size()>0){
			mDprocessInfos = new ArrayList<MDprocessInfo>();
			for (String string : processInfoJsons) {
				MDprocessInfo mDprocessInfo = JsonUtils.toObject(string, MDprocessInfo.class);
				mDprocessInfos.add(mDprocessInfo);
			}
		}else{
			if(jedis!=null){
            	jedis.close();
            }//将jedis连接放到redis连接池中
			return testingMessage;
		}
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
						//写到redis中
						jedis.set("businessCycle_"+testingMessage.getRemark(), cycleNum+1+"");
						break;
					}else{
						testingMessage.setBusinessCycle(1);
						//写到redis中
						jedis.set("businessCycle_"+testingMessage.getRemark(), 1+"");
						break;
					}
				}
			}
			if(jedis!=null){
            	jedis.close();
            }//将jedis连接放到redis连接池中
		}else{//如果不等于1
			//查看这条测试数据对应的工步的IS_CYCLE_SIGNAL_STEP 是否为true，如果为true则进行下面的操作，如果为false，则什么都不做
			bre:for (MDprocessInfo mDprocessInfo : mDprocessInfos) {
				if(testingMessage.getRemark()!=null && testingMessage.getRemark().equals(mDprocessInfo.getRemark())){//找到这个测试数据对应的流程
					List<MDStepInfo> mdStepInfoList = mDprocessInfo.getMdStepInfoList();//获取这个流程对应的所有的工步信息。
					for (MDStepInfo mdStepInfo : mdStepInfoList) {
						if(testingMessage.getStepId()==mdStepInfo.getStepId()){//找到这个测试信息对应的工步信息
							boolean isCycleSignalStep = mdStepInfo.isCycleSignalStep();
							if(isCycleSignalStep && testingMessage.getPvDataFlag()==89){//如果该测试数据对应的工步的 循环标志为 true 且 是一个工步的第一条数据
								String businessCycle = jedis.get("businessCycle_"+testingMessage.getRemark());
								if(!StringUtils.isNotBlank(businessCycle)){
									testingMessage.setBusinessCycle(1);
									jedis.set("businessCycle_"+testingMessage.getRemark(), 1+"");
								}else{
									int a = Integer.parseInt(businessCycle);
									testingMessage.setBusinessCycle(a+1);
									jedis.set("businessCycle_"+testingMessage.getRemark(), a+1+"");
								}
								break bre;
							}else{
								String businessCycle = jedis.get("businessCycle_"+testingMessage.getRemark());
								if(StringUtils.isNotBlank(businessCycle)){
									testingMessage.setBusinessCycle(Integer.parseInt(businessCycle));
								}
								break bre;
							}
						}
					}
				}
			}
		}
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
		return testingMessage;
	}
	
    /**
     * 维护工步的逻辑序号
     * @param testingMessage
     * @param cacheService
     */
	public TestingMessage updateStepLogicNumber(TestingMessage testingMessage, CacheService cacheService) {
		Jedis jedis = cacheService.getProxyJedisPool().getResource();
		int dataFlag = testingMessage.getPvDataFlag();//数据类型标识,能够表示工步起始点，工步终结点等信息（89代表起始，88代表终节点）
		if(dataFlag==89){
			String oldStepLogicNumber = jedis.get("stepLogicNumber_"+testingMessage.getRemark());
			if(!StringUtils.isNotBlank(oldStepLogicNumber)){
				testingMessage.setStepLogicNumber(1);
				jedis.set("stepLogicNumber_"+testingMessage.getRemark(), 1+"");
			}else{
				int a = Integer.parseInt(oldStepLogicNumber);
				testingMessage.setStepLogicNumber(a+1);
				jedis.set("stepLogicNumber_"+testingMessage.getRemark(), a+1+"");
			}
		}else{
			String oldStepLogicNumber = jedis.get("stepLogicNumber_"+testingMessage.getRemark());
			if(StringUtils.isNotBlank(oldStepLogicNumber)){
				testingMessage.setStepLogicNumber(Integer.parseInt(oldStepLogicNumber));
			}
		}
		if(jedis!=null){
        	jedis.close();
        }//将jedis连接放到redis连接池中
		return testingMessage;
	}
}
