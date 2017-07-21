package com.edcs.tds.storm.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.edcs.tds.common.engine.groovy.ContextConfig;
import com.edcs.tds.common.engine.groovy.service.EngineCommonService;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingSubChannel;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.MDStepInfo;
import com.edcs.tds.storm.model.MDprocessInfo;

import groovy.lang.Binding;

public class DataInit {
	
	public static final String SUBCHANNEL_NAME = "pvSubChannelData";//子通道的名称前缀
	
	private static final Logger logger = LoggerFactory.getLogger(DataInit.class);
    /**
     * 实现对测试数据（实时数据的初始化，将kafka拿出来的json测试数据转化为对应的java对象）
     * @param input
     * @return
     */
	public static TestingMessage initRequestMessage(Tuple input) {
		// TODO 实现DataInit，实现对Kafka测试数据的序列化；
		String json = null;
		try {
			json = new String((byte[])input.getValue(0),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			//FIXME 如果数据解析异常，直接扔掉，后续逻辑不走
			logger.error("实现对Kafka测试数据的序列化出现异常，异常位置为DataInit.initRequestMessage",e);
		}
		//如果传递过来的参数为空，则直接返回null
		if(!StringUtils.isNotBlank(json)){
			return null;
		}
		TestingMessage testingMsg = JsonUtils.toObject(json, TestingMessage.class);
		//设置测试数据的唯一id
		String msgId = testingMsg.getRemark()+testingMsg.getStepName()+testingMsg.getSequenceId();
		testingMsg.setMessageId(msgId);
		
		JSONObject jsonObject = JsonUtils.parseObject(json);
		
		int i = 0;//来标注有多少子通道的。
		List<TestingSubChannel> lists = new ArrayList<TestingSubChannel>();//用来存放子通道信息
		while(true){
			i++;
			String str = jsonObject.getString(SUBCHANNEL_NAME+i);
			if("0".equals(str)){
				continue;
			}
			JSONObject subJson = jsonObject.getJSONObject(SUBCHANNEL_NAME+i);
			if(subJson == null){
				break;
			}else{
				String strJson = JSONObject.toJSONString(subJson);//获取子通道信息
				TestingSubChannel testingSubChannel = JsonUtils.toObject(strJson, TestingSubChannel.class);//转化为子通道的对象
				testingSubChannel.setSubChannelName(SUBCHANNEL_NAME+i);
				lists.add(testingSubChannel);
			}
		}
		testingMsg.setSubChannel(lists);
		return testingMsg;
	}

	public static void initShellContext(TestingMessage testingMessage, EngineCommonService engineCommonService,
			ExecuteContext executeContext, Binding shellContext) {
		shellContext.setVariable(ContextConfig.ENGINE_COMMON_SERVICE, engineCommonService);
	}
    /**
     * 数据初始化
     * @param executeContext
     * @param cacheService
     * @param shellContext
     * @return 
     */
	public static Binding initShellContext(ExecuteContext executeContext, CacheService cacheService,
			Binding shellContext) {
		// TODO 取测试数据和主数据中的数据来初始化 shellContext
		TestingMessage testingMsg = executeContext.getTestingMessage();//获取测试数据
		Set<String> processInfoJsons = cacheService.getProcessInfoJsons();//获取流程主数据 信息
		MDprocessInfo mDprocessInfo = null;
		if(processInfoJsons!=null && processInfoJsons.size()>0){
			for (String processInfoJson : processInfoJsons) {
				MDprocessInfo mDprocessInfo2 = JsonUtils.toObject(processInfoJson, MDprocessInfo.class);
				if(mDprocessInfo2.getRemark().equals(testingMsg.getRemark())){//获取当前测试数据对应的流程主数据。
					mDprocessInfo = mDprocessInfo2;
					break;
				}
			}
		}
		if(mDprocessInfo!=null){
			
			//将是否是一个公布的第一条测试数据和是否是最后一条测试数据的标志传递给规则配置中
			shellContext.setProperty("dataState", testingMsg.getPvDataFlag());
			
			/*
			 * 电流测试场景需要的参数
			 */
			shellContext.setProperty("pvCurrent", testingMsg.getPvCurrent());//需要用来比较的电流
			
			shellContext.setProperty("svIcRange", testingMsg.getSvIcRange());//电流通道最大流程
			
			// CCCC 恒流放电     DDDD 恒流充电
			
			//获取这个流程的所有工 步信息
			List<MDStepInfo> mdStepInfos = mDprocessInfo.getMdStepInfoList();
			for (MDStepInfo mdStepInfo : mdStepInfos) {
                //FIXME 中文常量或固定的名称统一移到com.edcs.tds.common.util.SystemConfig
				if(("恒压充电".equals(mdStepInfo.getStepName()) && "恒压充电".equals(testingMsg.getStepName()))
						|| ("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName()))
						|| ("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
						|| ("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
						|| ("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
						|| ("DDDD".equals(mdStepInfo.getStepName()) && "DDDD".equals(testingMsg.getStepName()))
						|| ("CCCC".equals(mdStepInfo.getStepName()) && "CCCC".equals(testingMsg.getStepName()))){
					shellContext.setProperty("svStepEndCurrent", mdStepInfo.getSvStepEndCurrent());//恒压充电工步的 截止电流 （I截止）
					shellContext.setProperty("svCurrent", mdStepInfo.getSvCurrent());//Ilast为工步最后一点电流值
				}
				if(("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
						|| ("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
						|| ("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
						|| ("CCCC".equals(mdStepInfo.getStepName()) && "CCCC".equals(testingMsg.getStepName()))){
					shellContext.setProperty("svStepEndVoltage", mdStepInfo.getSvStepEndVoltage());//U截止 为恒功率充电截止电压 (U截止)
					shellContext.setProperty("svPower", mdStepInfo.getSvPower());//（P恒 冲） （P恒 放）
				}
				if("模拟工步（电流模式）".equals(mdStepInfo.getStepName()) && "模拟工步（电流模式）".equals(testingMsg.getStepName())){
					shellContext.setProperty("svCurrent", mdStepInfo.getSvCurrent());//I为工况附录文件中规定的电流
					shellContext.setProperty("svPower", mdStepInfo.getSvPower());//模拟工步（电流模式）中的（P恒）
				}
				if(("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
						|| ("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))){
					TestingMessage upTestingMsg  = GetDataInterface.getUpTestingMsg(testingMsg, 1, cacheService);//获取上一条测试数据信息
					if(upTestingMsg!=null){
						shellContext.setProperty("upPvCurrent", upTestingMsg.getPvCurrent());//I i-1 为上一条数据的电流值
					}
				}
			}
			//R恒 为流程设置恒阻值
			shellContext.setProperty("constantIrValue", mDprocessInfo.getConstantIrValue());
			
			/*
			 * 电压测试场景需要的参数
			 */
			shellContext.setProperty("pvVoltage", testingMsg.getPvVoltage());//需要用来比较的电压
			shellContext.setProperty("svUpperU", mDprocessInfo.getSvUpperU());//U上限 为测试流程中规定的上限电压
			shellContext.setProperty("svLowerU", mDprocessInfo.getSvLowerU());//U下限为测试流程中规定的上限电压
			
			TestingMessage firstTestingMsg = GetDataInterface.getFirstTestingMsg(testingMsg,cacheService);//
			if(firstTestingMsg!=null){
				shellContext.setProperty("pvVoltageFirst", firstTestingMsg.getPvVoltage());//U1
			}
			TestingMessage upTestingMsg  = GetDataInterface.getUpTestingMsg(testingMsg, 1, cacheService);//获取上一条测试数据信息
			if(upTestingMsg!=null){
				shellContext.setProperty("upPvVoltage", upTestingMsg.getPvVoltage());//U i-1 为上一条数据的电压值
			}
			
			TestingMessage upStepTestingMsg = GetDataInterface.getUpStepTestingMsg(testingMsg, 1, cacheService);//获取上一个工步的最后一条测试数据
			if(upStepTestingMsg!=null){
				shellContext.setProperty("upStepPvVoltage", upStepTestingMsg.getPvVoltage());//U 放电末 、U 冲电末
			}
			
			for (MDStepInfo mdStepInfo : mdStepInfos) {
				if("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName())
						|| "CCCC".equals(mdStepInfo.getStepName()) && "CCCC".equals(testingMsg.getStepName())
						|| "DDDD".equals(mdStepInfo.getStepName()) && "DDDD".equals(testingMsg.getStepName())
						|| "恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName())
						|| "恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName())
						|| "恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName())){
					shellContext.setProperty("svVoltage", mdStepInfo.getSvVoltage());//U恒压 为恒压阶段设定电压值
				}
			}
			
			/*
			 * 容量测试场景需要的参数
			 */
			if("CCCC".equals(testingMsg.getStepName()) 
					||"恒功率放电".equals(testingMsg.getStepName())
					||"恒阻放电".equals(testingMsg.getStepName())){
				shellContext.setProperty("pvCapacity", testingMsg.getPvDischargeCapacity());//Ci为工步实时容量值
			}
			if("DDDD".equals(testingMsg.getStepName()) 
					||"恒压充电".equals(testingMsg.getStepName())
					||"恒流恒压充电".equals(testingMsg.getStepName())
					||"恒功率充电".equals(testingMsg.getStepName())){
				shellContext.setProperty("pvCapacity", testingMsg.getPvChargeCapacity());//Ci为工步实时容量值
			}
			for (MDStepInfo mdStepInfo : mdStepInfos) {
				if(("CCCC".equals(mdStepInfo.getStepName()) && "CCCC".equals(testingMsg.getStepName())) 
						||("DDDD".equals(mdStepInfo.getStepName()) && "DDDD".equals(testingMsg.getStepName())) 
						||("恒压充电".equals(mdStepInfo.getStepName()) && "恒压充电".equals(testingMsg.getStepName()))
						||("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName()))
						||("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
						||("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
						||("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
						||("模拟工步（电流模式）".equals(mdStepInfo.getStepName()) && "模拟工步（电流模式）".equals(testingMsg.getStepName()))
						||("模拟工步（功率模式）".equals(mdStepInfo.getStepName()) && "模拟工步（功率模式）".equals(testingMsg.getStepName()))){
					shellContext.setProperty("svStepEndCapacity", mdStepInfo.getSvStepEndCapacity());//C设定 为工步设定截止容量
					shellContext.setProperty("svCapacity", mdStepInfo.getSvCapacity());//Clast 为工步最后一点容量值
				}
			}
			shellContext.setProperty("svCapacityValue", mDprocessInfo.getSvCapacityValue());//C为电芯标称容量
			
			/*
			 * 能量（暂时不做）
			 */
//			if("恒流放电".equals(testingMsg.getStepName()) 
//					||"恒功率放电".equals(testingMsg.getStepName())
//					||"恒阻放电".equals(testingMsg.getStepName())){
//				shellContext.setProperty("pvEnergy", testingMsg.getPvDischargeEnergy());//Ei为工步实时能量值
//			}
//			if("恒流充电".equals(testingMsg.getStepName()) 
//					||"恒压充电".equals(testingMsg.getStepName())
//					||"恒流恒压充电".equals(testingMsg.getStepName())
//					||"恒功率充电".equals(testingMsg.getStepName())){
//				shellContext.setProperty("pvEnergy", testingMsg.getPvChargeEnergy());//Ei为工步实时能量值
//			}
//			
//			
//			for (MDStepInfo mdStepInfo : mdStepInfos) {
//				if(("恒流放电".equals(mdStepInfo.getStepName()) && "恒流放电".equals(testingMsg.getStepName())) 
//						||("恒流充电".equals(mdStepInfo.getStepName()) && "恒流充电".equals(testingMsg.getStepName())) 
//						||("恒压充电".equals(mdStepInfo.getStepName()) && "恒压充电".equals(testingMsg.getStepName()))
//						||("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName()))
//						||("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
//						||("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
//						||("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
//						||("模拟工步（电流模式）".equals(mdStepInfo.getStepName()) && "模拟工步（电流模式）".equals(testingMsg.getStepName()))
//						||("模拟工步（功率模式）".equals(mdStepInfo.getStepName()) && "模拟工步（功率模式）".equals(testingMsg.getStepName()))){
//					shellContext.setProperty("svEnergy", mdStepInfo.getSvEnergy());//E为电芯额定能量
//					
//				}
//			}
			
			/*
			 * 温度
			 */
			
//			shellContext.setProperty("pvTemperature", testingMsg.getPvTemperature());//用来比较的温度
//			for (MDStepInfo mdStepInfo : mdStepInfos) {
//				if(("搁置（Start）".equals(mdStepInfo.getStepName()) && "搁置（Start）".equals(testingMsg.getStepName()))
//						||("搁置（A-DC）".equals(mdStepInfo.getStepName()) && "搁置（A-DC）".equals(testingMsg.getStepName()))
//						||("搁置（A-CC）".equals(mdStepInfo.getStepName()) && "搁置（A-CC）".equals(testingMsg.getStepName()))
//						
//						||("恒流放电".equals(mdStepInfo.getStepName()) && "恒流放电".equals(testingMsg.getStepName())) 
//						||("恒流充电".equals(mdStepInfo.getStepName()) && "恒流充电".equals(testingMsg.getStepName())) 
//						||("恒压充电".equals(mdStepInfo.getStepName()) && "恒压充电".equals(testingMsg.getStepName()))
//						||("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName()))
//						||("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
//						||("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
//						||("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
//						||("模拟工步（电流模式）".equals(mdStepInfo.getStepName()) && "模拟工步（电流模式）".equals(testingMsg.getStepName()))
//						||("模拟工步（功率模式）".equals(mdStepInfo.getStepName()) && "模拟工步（功率模式）".equals(testingMsg.getStepName()))){
//					shellContext.setProperty("svTemperature", mdStepInfo.getSvTemperature());//工步设定温度由测试申请单导入TDP系统
//					
//				}
//			}
			
			/**
			 * 相对时间
			 */
			shellContext.setProperty("testTimeDuration", testingMsg.getTestTimeDuration());//需要用来比较的相对时间
			
			for (MDStepInfo mdStepInfo : mdStepInfos) {
				if(("搁置（Start）".equals(mdStepInfo.getStepName()) && "搁置（Start）".equals(testingMsg.getStepName()))
						||("搁置（A-DC）".equals(mdStepInfo.getStepName()) && "搁置（A-DC）".equals(testingMsg.getStepName()))
						||("搁置（A-CC）".equals(mdStepInfo.getStepName()) && "搁置（A-CC）".equals(testingMsg.getStepName()))
						
						||("CCCC".equals(mdStepInfo.getStepName()) && "CCCC".equals(testingMsg.getStepName())) 
						||("DDDD".equals(mdStepInfo.getStepName()) && "DDDD".equals(testingMsg.getStepName())) 
						||("恒压充电".equals(mdStepInfo.getStepName()) && "恒压充电".equals(testingMsg.getStepName()))
						||("恒流恒压充电".equals(mdStepInfo.getStepName()) && "恒流恒压充电".equals(testingMsg.getStepName()))
						||("恒功率充电".equals(mdStepInfo.getStepName()) && "恒功率充电".equals(testingMsg.getStepName()))
						||("恒功率放电".equals(mdStepInfo.getStepName()) && "恒功率放电".equals(testingMsg.getStepName()))
						||("恒阻放电".equals(mdStepInfo.getStepName()) && "恒阻放电".equals(testingMsg.getStepName()))
						||("模拟工步（电流模式）".equals(mdStepInfo.getStepName()) && "模拟工步（电流模式）".equals(testingMsg.getStepName()))
						||("模拟工步（功率模式）".equals(mdStepInfo.getStepName()) && "模拟工步（功率模式）".equals(testingMsg.getStepName()))){
					shellContext.setProperty("svTime", mdStepInfo.getSvTime());//工步的相对时间
					
				}
			}
		}
		return shellContext;
	}
}
