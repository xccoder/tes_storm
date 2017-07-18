package com.edcs.tds.common.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
/**
 * 测试数据的实体类（实时数据）
 * @author LiQF
 *
 */
public class TestingMessage implements Serializable {

	private static final long serialVersionUID = -7703529162886545641L;

	// TODO 修改为实际的数据类型
	private String messageId; //<SITE>,<REMARK>,<SFC>,<CATEGORY>,<ALERT_SEQUENCE_NUMBER>
	private boolean isDebug;
	private String sfc;//电芯号(SFC) barcode
	private String remark;//流程文件标识号：T3-20160415-1317-246454_DCR_35（测试单号+流程名称+电芯号）要求是全局唯一号
	private BigDecimal svIcRange;//电流量程：正在进行测试的通道的电流量程。用以作为判异阈值计算输入（通道最大量程）
	private BigDecimal svIvRange;//电压量程：正在进行测试的通道的电压量程。用以作为判异阈值计算输入
	private int channelId;//通道号
	private int sequenceId;//记录序号(每一条数据的序号，一个流程这个记录序号一直在累加)
	private int cycle;//循环序号（一个流程中工步与工步之间的循环序号。这个序号是有可能出现问题的，需要处理）(后期可能不用这个字段，因为strom系统上线之后可能机器已经在运行了。可能使用  businessCycle)
	private int stepId;//工步序号
	private int stepLogicNumber;//工步的逻辑序号
	private String stepName;//工步名称
	private BigDecimal testTimeDuration;//测试相对时长
	private BigDecimal pvVoltage;//电压
	private BigDecimal pvCurrent;//电流
	private BigDecimal pvIr;//内阻
	private BigDecimal pvTemperature;//温度  temperature
	private BigDecimal pvChargeCapacity;//充电容量  充电容量和放电容量一定有一个为0   ccap
	private BigDecimal pvDischargeCapacity;//放电容量 dccap
	private BigDecimal pvChargeEnergy;//充电能量
	private BigDecimal pvDischargeEnergy;//放电能量 dceng
	private Timestamp timestamp;//测试绝对时间 absTime
	private List<TestingSubChannel> subChannel;//辅助通道
	private String resourceId;//设备号
	private int pvDataFlag;//数据类型标识,能够表示工步起始点，工步终结点等信息（89代表起始，88 代表终节点）
	private int pvWorkType;//工作状态。正常情况下标识为正常测试状态。能够标识测试的停止，完成，保护等状态(数字) 0  表示测试完成
	private int businessCycle;//业务循环号。
	
	
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
	public String getSfc() {
		return sfc;
	}
	public void setSfc(String sfc) {
		this.sfc = sfc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public BigDecimal getSvIcRange() {
		return svIcRange;
	}
	public void setSvIcRange(BigDecimal svIcRange) {
		this.svIcRange = svIcRange;
	}
	public BigDecimal getSvIvRange() {
		return svIvRange;
	}
	public void setSvIvRange(BigDecimal svIvRange) {
		this.svIvRange = svIvRange;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(int sequenceId) {
		this.sequenceId = sequenceId;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getStepLogicNumber() {
		return stepLogicNumber;
	}
	public void setStepLogicNumber(int stepLogicNumber) {
		this.stepLogicNumber = stepLogicNumber;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}
	public BigDecimal getTestTimeDuration() {
		return testTimeDuration;
	}
	public void setTestTimeDuration(BigDecimal testTimeDuration) {
		this.testTimeDuration = testTimeDuration;
	}
	public BigDecimal getPvVoltage() {
		return pvVoltage;
	}
	public void setPvVoltage(BigDecimal pvVoltage) {
		this.pvVoltage = pvVoltage;
	}
	public BigDecimal getPvCurrent() {
		return pvCurrent;
	}
	public void setPvCurrent(BigDecimal pvCurrent) {
		this.pvCurrent = pvCurrent;
	}
	public BigDecimal getPvIr() {
		return pvIr;
	}
	public void setPvIr(BigDecimal pvIr) {
		this.pvIr = pvIr;
	}
	public BigDecimal getPvTemperature() {
		return pvTemperature;
	}
	public void setPvTemperature(BigDecimal pvTemperature) {
		this.pvTemperature = pvTemperature;
	}
	public BigDecimal getPvChargeCapacity() {
		return pvChargeCapacity;
	}
	public void setPvChargeCapacity(BigDecimal pvChargeCapacity) {
		this.pvChargeCapacity = pvChargeCapacity;
	}
	public BigDecimal getPvDischargeCapacity() {
		return pvDischargeCapacity;
	}
	public void setPvDischargeCapacity(BigDecimal pvDischargeCapacity) {
		this.pvDischargeCapacity = pvDischargeCapacity;
	}
	public BigDecimal getPvChargeEnergy() {
		return pvChargeEnergy;
	}
	public void setPvChargeEnergy(BigDecimal pvChargeEnergy) {
		this.pvChargeEnergy = pvChargeEnergy;
	}
	public BigDecimal getPvDischargeEnergy() {
		return pvDischargeEnergy;
	}
	public void setPvDischargeEnergy(BigDecimal pvDischargeEnergy) {
		this.pvDischargeEnergy = pvDischargeEnergy;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
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
	public int getPvDataFlag() {
		return pvDataFlag;
	}
	public void setPvDataFlag(int pvDataFlag) {
		this.pvDataFlag = pvDataFlag;
	}
	public int getPvWorkType() {
		return pvWorkType;
	}
	public void setPvWorkType(int pvWorkType) {
		this.pvWorkType = pvWorkType;
	}
	public int getBusinessCycle() {
		return businessCycle;
	}
	public void setBusinessCycle(int businessCycle) {
		this.businessCycle = businessCycle;
	}
}
