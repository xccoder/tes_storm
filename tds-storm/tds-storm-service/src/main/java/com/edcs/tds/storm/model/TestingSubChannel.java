package com.edcs.tds.storm.model;

import java.io.Serializable;
/**
 * 测试数据中的辅助通道的实体类
 * @author LiQF
 *
 */
import java.math.BigDecimal;
import java.sql.Date;
public class TestingSubChannel implements Serializable {

	private static final long serialVersionUID = 4863558373163111458L;

	// TODO 补充实际的字段
	private int sequenceId;//记录序号(每一条数据的序号，一个流程这个记录序号一直在累加)
	private int cycle;//循环序号（一个流程中工步与工步之间的循环序号。这个序号是有可能出现问题的，需要处理）(后期可能不用这个字段，因为strom系统上线之后可能机器已经在运行了。)
	private int stepId;//工步序号
	private BigDecimal testTimeDuration;//测试相对时长
	private BigDecimal voltage;//电压
	private BigDecimal current;//电流
	private BigDecimal ir;//内阻
	private BigDecimal temperature;//温度
	private BigDecimal chargeCapacity;//充电容量  充电容量和放电容量一定有一个为0
    private BigDecimal dischargeCapacity;//放电容量
    private BigDecimal chargeEnergy;//充电能量
    private BigDecimal dischargeEnergy;//放电能量
    private Date timestamp;// "2017-03-21 18:07:43";绝对时间
    private int dataFlag;//数据类型标识,能够表示工步起始点，工步终结点等信息
    private String workType;//工作状态。正常情况下标识为正常测试状态。能够标识测试的停止，完成，保护等状态
	
    
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
	public BigDecimal getTestTimeDuration() {
		return testTimeDuration;
	}
	public void setTestTimeDuration(BigDecimal testTimeDuration) {
		this.testTimeDuration = testTimeDuration;
	}
	public BigDecimal getVoltage() {
		return voltage;
	}
	public void setVoltage(BigDecimal voltage) {
		this.voltage = voltage;
	}
	public BigDecimal getCurrent() {
		return current;
	}
	public void setCurrent(BigDecimal current) {
		this.current = current;
	}
	public BigDecimal getIr() {
		return ir;
	}
	public void setIr(BigDecimal ir) {
		this.ir = ir;
	}
	public BigDecimal getTemperature() {
		return temperature;
	}
	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}
	public BigDecimal getChargeCapacity() {
		return chargeCapacity;
	}
	public void setChargeCapacity(BigDecimal chargeCapacity) {
		this.chargeCapacity = chargeCapacity;
	}
	public BigDecimal getDischargeCapacity() {
		return dischargeCapacity;
	}
	public void setDischargeCapacity(BigDecimal dischargeCapacity) {
		this.dischargeCapacity = dischargeCapacity;
	}
	public BigDecimal getChargeEnergy() {
		return chargeEnergy;
	}
	public void setChargeEnergy(BigDecimal chargeEnergy) {
		this.chargeEnergy = chargeEnergy;
	}
	public BigDecimal getDischargeEnergy() {
		return dischargeEnergy;
	}
	public void setDischargeEnergy(BigDecimal dischargeEnergy) {
		this.dischargeEnergy = dischargeEnergy;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public int getDataFlag() {
		return dataFlag;
	}
	public void setDataFlag(int dataFlag) {
		this.dataFlag = dataFlag;
	}
	public String getWorkType() {
		return workType;
	}
	public void setWorkType(String workType) {
		this.workType = workType;
	}
    
}
