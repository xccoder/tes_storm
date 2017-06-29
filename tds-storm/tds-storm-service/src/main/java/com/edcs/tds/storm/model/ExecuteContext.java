package com.edcs.tds.storm.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.edcs.tds.common.model.RuleConfig;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ExecuteContext {

	private final Logger logger = LoggerFactory.getLogger(ExecuteContext.class);

	private boolean debug = false;

	private StringBuilder errorMessage = new StringBuilder();

	private StringBuilder debugMessage = new StringBuilder();

	private ConcurrentMap<Long, Pair<Double, Long>> matchedRules = Maps.newConcurrentMap();

	private TestingMessage testingMessage;

	public TestingMessage getTestingMessage() {
		return testingMessage;
	}

	public void setTestingMessage(TestingMessage testingMessage) {
		this.testingMessage = testingMessage;
	}

	private List<Pair<String, Long>> timePointLine = Lists.newArrayList();

	private long execUsedTime = 0;

	private ConcurrentMap<Long, String> variableLog = Maps.newConcurrentMap();

	private Map<String, Object> sysVariableLog = Maps.newLinkedHashMap();

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return debug;
	}

	public long getExecUsedTime() {
		return execUsedTime;
	}

	public void setExecUsedTime(long execUsedTime) {
		this.execUsedTime = execUsedTime;
	}

	public void addException(Exception ex) {
		errorMessage.append("[");
		errorMessage.append(ExceptionUtils.getStackTrace(ex));
		errorMessage.append("]\n");
	}

	public void addRuleException(RuleConfig model, Exception ex, long executeUsedTime) {
		errorMessage.append("[规则ID: ");
		errorMessage.append(model.getId());
		errorMessage.append(" --> ");
		errorMessage.append(ExceptionUtils.getStackTrace(ex));
		errorMessage.append("] 执行使用时间: [");
		errorMessage.append(executeUsedTime);
		errorMessage.append(" ms]\n");
	}

	public void addDebugMessage(String message) {
		if (debug) {
			debugMessage.append(message);
			debugMessage.append("\n");
		}
	}

	public void addDebugMessage(String message, Object... args) {
		if (debug) {
			if (args != null && args.length > 0) {
				debugMessage.append(String.format(message, args));
			} else {
				debugMessage.append(message);
			}
			debugMessage.append("\n");
		}
	}

	public void addTimePointLine(String point, long time) {
		timePointLine.add(Pair.of(point, time));
	}

	public StringBuilder calcTimePointLine() {
		StringBuilder builder = new StringBuilder("");
		if (timePointLine.size() >= 2) {
			long start = timePointLine.get(0).getRight();
			long end = timePointLine.get(timePointLine.size() - 1).getRight();
			this.execUsedTime = end - start;
			if (this.execUsedTime >= 800) {
				builder = new StringBuilder("[ ");
				builder.append(timePointLine.get(0).getLeft());
				for (int i = 1; i < timePointLine.size(); i++) {
					builder.append(" --> ")
							.append(timePointLine.get(i).getRight() - timePointLine.get(i - 1).getRight())
							.append(" ms");
					builder.append(" --> ").append(timePointLine.get(i).getLeft());
				}
				builder.append(" | Used --> ").append(this.execUsedTime).append(" ms ]");
				logger.warn("! CalcBolt core calculation time is too long: {}", builder.toString());
			}
		}
		return builder;
	}

	public StringBuilder getErrorMessage() {
		return errorMessage;
	}

	public StringBuilder getDebugMessage() {
		return debugMessage;
	}

	public void addMatchedRule(Long ruleId, Double result, long executeUsedTime) {
		matchedRules.put(ruleId, Pair.of(result, executeUsedTime));
	}

	public ConcurrentMap<Long, Pair<Double, Long>> getMatchedRules() {
		return matchedRules;
	}

	public String getVariableLog() {
		return JSON.toJSONString(variableLog);
	}

	public void addVariableLog(Long ruleId, String log) {
		variableLog.put(ruleId, log);
	}

	public String getSysVariableLog() {
		return JSON.toJSONString(sysVariableLog);
	}

	public void setSysVariableLog(Map<String, Object> sysVariableLog) {
		this.sysVariableLog = sysVariableLog;
	}

}
