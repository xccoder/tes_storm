package com.edcs.tds.storm.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.edcs.tds.common.engine.groovy.ContextConfig;
import com.edcs.tds.common.engine.groovy.service.EngineCommonService;
import com.edcs.tds.common.model.TestingMessage;
import com.edcs.tds.common.model.TestingSubChannel;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.MDprocessInfo;

import backtype.storm.tuple.Tuple;
import groovy.lang.Binding;

public class DataInit {
	
	public static final String SUBCHANNEL_NAME = "pvSubChannelData";//子通道的名称前缀
    /**
     * 实现对测试数据（实时数据的初始化，将kafka拿出来的json测试数据转化为对应的java对象）
     * @param input
     * @return
     */
	public static TestingMessage initRequestMessage(Tuple input) {
//		@SuppressWarnings("unchecked")
//		Map<String, String> testingDataMap = (Map<String, String>) DataSerializer
//				.asObjectForDefault((byte[]) input.getValue(0));
		// TODO 实现DataInit，实现对Kafka测试数据的序列化；
		String json = (String)input.getValue(0);
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
		String processInfoJson = cacheService.getProcessInfoJson();//获取流程主数据json
		
		MDprocessInfo mDprocessInfo = null;
		if(StringUtils.isNotBlank(processInfoJson)){
			List<MDprocessInfo> mDprocessInfos = JsonUtils.toArray(processInfoJson, MDprocessInfo.class);//转化为流程主数据的model
			for (MDprocessInfo mDprocessInfo2 : mDprocessInfos) {
				if(mDprocessInfo2.getRemark().equals(testingMsg.getRemark())){//获取当前测试数据对应的流程主数据。
					mDprocessInfo = mDprocessInfo2;
					break;
				}
			}
		}
		if(mDprocessInfo!=null){
			shellContext.setProperty("", 0);
			
		}
		return shellContext;
	}
}
