package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.util.List;

public class TestingMessage implements Serializable {

	private static final long serialVersionUID = -7703529162886545641L;

	// TODO 修改为实际的数据类型
	private String messageId;
	private boolean isDebug;
	private String barcode;
	private String remark;
	private String icRange;
	private String ivRange;
	private String channelId;
	private String sequenceId;
	private Integer cycle;
	private String setpId;
	private String setpName;
	private String testTime;
	private String volt;
	private String curr;
	private String ir;
	private String temperature;
	private String ccap;
	private String dccap;
	private String ceng;
	private String dceng;
	private String absTime;
	private List<TestingSubChannel> subChannel;
	private String resourceId;
	private String dataFlag;
	private String workType;
	private Integer businessCycle;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIcRange() {
		return icRange;
	}

	public void setIcRange(String icRange) {
		this.icRange = icRange;
	}

	public String getIvRange() {
		return ivRange;
	}

	public void setIvRange(String ivRange) {
		this.ivRange = ivRange;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public Integer getCycle() {
		return cycle;
	}

	public void setCycle(Integer cycle) {
		this.cycle = cycle;
	}

	public String getSetpId() {
		return setpId;
	}

	public void setSetpId(String setpId) {
		this.setpId = setpId;
	}

	public String getSetpName() {
		return setpName;
	}

	public void setSetpName(String setpName) {
		this.setpName = setpName;
	}

	public String getTestTime() {
		return testTime;
	}

	public void setTestTime(String testTime) {
		this.testTime = testTime;
	}

	public String getVolt() {
		return volt;
	}

	public void setVolt(String volt) {
		this.volt = volt;
	}

	public String getCurr() {
		return curr;
	}

	public void setCurr(String curr) {
		this.curr = curr;
	}

	public String getIr() {
		return ir;
	}

	public void setIr(String ir) {
		this.ir = ir;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getCcap() {
		return ccap;
	}

	public void setCcap(String ccap) {
		this.ccap = ccap;
	}

	public String getDccap() {
		return dccap;
	}

	public void setDccap(String dccap) {
		this.dccap = dccap;
	}

	public String getCeng() {
		return ceng;
	}

	public void setCeng(String ceng) {
		this.ceng = ceng;
	}

	public String getDceng() {
		return dceng;
	}

	public void setDceng(String dceng) {
		this.dceng = dceng;
	}

	public String getAbsTime() {
		return absTime;
	}

	public void setAbsTime(String absTime) {
		this.absTime = absTime;
	}

	public List<TestingSubChannel> getSubChannel() {
		return subChannel;
	}

	public void setSubChannel(List<TestingSubChannel> subChannel) {
		this.subChannel = subChannel;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getDataFlag() {
		return dataFlag;
	}

	public void setDataFlag(String dataFlag) {
		this.dataFlag = dataFlag;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public Integer getBusinessCycle() {
		return businessCycle;
	}

	public void setBusinessCycle(Integer businessCycle) {
		this.businessCycle = businessCycle;
	}

	public TestingMessage() {
		super();
	}

	public TestingMessage(String messageId, boolean isDebug, String barcode, String remark, String icRange,
			String ivRange, String channelId, String sequenceId, Integer cycle, String setpId, String setpName,
			String testTime, String volt, String curr, String ir, String temperature, String ccap, String dccap,
			String ceng, String dceng, String absTime, List<TestingSubChannel> subChannel, String resourceId,
			String dataFlag, String workType) {
		super();
		this.messageId = messageId;
		this.isDebug = isDebug;
		this.barcode = barcode;
		this.remark = remark;
		this.icRange = icRange;
		this.ivRange = ivRange;
		this.channelId = channelId;
		this.sequenceId = sequenceId;
		this.cycle = cycle;
		this.setpId = setpId;
		this.setpName = setpName;
		this.testTime = testTime;
		this.volt = volt;
		this.curr = curr;
		this.ir = ir;
		this.temperature = temperature;
		this.ccap = ccap;
		this.dccap = dccap;
		this.ceng = ceng;
		this.dceng = dceng;
		this.absTime = absTime;
		this.subChannel = subChannel;
		this.resourceId = resourceId;
		this.dataFlag = dataFlag;
		this.workType = workType;
	}

}
