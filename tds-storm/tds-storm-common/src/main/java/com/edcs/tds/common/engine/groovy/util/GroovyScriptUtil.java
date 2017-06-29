package com.edcs.tds.common.engine.groovy.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.edcs.tds.common.engine.groovy.exception.GroovyCheckError;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

/**
 * Groovy 语法验证工具类
 * 
 * @author WJ
 *
 */
public class GroovyScriptUtil {

	private static final Logger logger = LoggerFactory.getLogger(GroovyScriptUtil.class);

	public static boolean test(String scriptName, StringBuffer script, Map<String, Object> parameter) throws GroovyCheckError {
		boolean state = false;
		try {
			logger.debug("Starting check groovy script...");
			GroovyShell groovyShell = new GroovyShell(new Binding());
			Script scriptObj = groovyShell.parse(script.toString());
			if (scriptObj != null) {
				state = true;
			} else {
				throw new GroovyCheckError("规则编译检查错误");
			}
			logger.debug("Groovy shell test done, script name:[" + scriptName + "], result: " + JSON.toJSONString(scriptObj));
		} catch (Exception e) {
			logger.error(e.toString());
			throw new GroovyCheckError("规则编译检查错误：" + e.toString());
		}
		return state;
	}

}