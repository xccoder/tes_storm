package com.edcs.tds.storm.service;

import java.util.Map;

import com.edcs.tds.common.engine.groovy.ContextConfig;
import com.edcs.tds.common.engine.groovy.service.EngineCommonService;
import com.edcs.tds.storm.model.ExecuteContext;
import com.edcs.tds.storm.model.TestingMessage;
import com.edcs.tds.storm.util.DataSerializer;

import backtype.storm.tuple.Tuple;
import groovy.lang.Binding;

public class DataInit {

	public static TestingMessage initRequestMessage(Tuple input) {
		@SuppressWarnings("unchecked")
		Map<String, String> testingDataMap = (Map<String, String>) DataSerializer
				.asObjectForDefault((byte[]) input.getValue(0));
		// TODO 实现DataInit，实现对Kafka测试数据的序列化；
		return null;
	}

	public static void initShellContext(TestingMessage testingMessage, EngineCommonService engineCommonService,
			ExecuteContext executeContext, Binding shellContext) {
		shellContext.setVariable(ContextConfig.ENGINE_COMMON_SERVICE, engineCommonService);
	}
}
