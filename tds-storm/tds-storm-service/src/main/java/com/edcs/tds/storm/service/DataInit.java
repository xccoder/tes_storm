package com.edcs.tds.storm.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.edcs.tds.common.engine.groovy.ContextConfig;
import com.edcs.tds.common.engine.groovy.service.EngineCommonService;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.TestingMessage;
import com.edcs.tds.storm.model.TestingSubChannel;
import com.edcs.tds.storm.util.JsonUtils;

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
		String msgId = testingMsg.getRemark()+testingMsg.getSetpName()+testingMsg.getSequenceId();
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
}
