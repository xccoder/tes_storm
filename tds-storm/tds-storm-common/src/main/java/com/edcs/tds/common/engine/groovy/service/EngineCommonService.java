package com.edcs.tds.common.engine.groovy.service;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Groovy 通用接口,用于规则脚本内做业务逻辑使用
 */
public class EngineCommonService implements Serializable {

	private static final long serialVersionUID = -1091574597489140388L;

	private static final Logger logger = LoggerFactory.getLogger(EngineCommonService.class);

	@Deprecated // 测试使用，可以删除
	public String defaultTestAPI(String text) {
		String result = "defaultTestAPI: " + text;
		logger.debug(result);
		return result;
	}

	public Double getLastResultBySetp(int setpId) {
		// TODO 指定工步的上一条历史记录
		return 0d;
	}

}