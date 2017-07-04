package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 测试结果数据实体类（以一个算法为单位，一个算法对应一个这样的实体类）
 * @author LiQF
 *
 */
public class TestingResultData implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7962734736148641268L;
	
	private String handle;//<SITE>,<REMARK>,<SFC>,<CATEGORY>,<ALERT_SEQUENCE_NUMBER>
	private String site;
	private String remark;//流程编号
	private int stepId;//工步序号
	private int businessCycle;//业务循环号。
	private int cycle;//设备循环号
	private String sfc;//电芯号
	private String category;//类别（电流，电压。。。。）
	private int altetSequenceNumber;//序列值（同一个工步之间报警的序号）
	private String txAlertListInfoBO;//TX_ALERT_LIST_INFO.HANDLE
	private String status;//状态，new  、close
	private String processDataBO;//MD_PROCESS_DATA.HANDLE
	private Timestamp timestamp;//记录报警时间（什么时候报警的）
	private String erpResourceBO;//ERP_RESOURCE.HANDLE
	private int channelId;//通道号
	private int alertLevel;//报警级别
	private String description;//报警信息描述
	private BigDecimal upLimit;//报警上限
	private BigDecimal lowLimit;//报警下限
	private String originalProcessDataBO;//TX_ORIGINAL_PROCESS_DATA.HANDLE
	private Timestamp createdDateTime;//创建时间
	private String createdUser;//创建用户
	private Timestamp modifiedDateTime;//最后修改日期
	private String modifiedUser;//最后修改用户
	
	public String getHadele() {
		return handle;
	}
	public void setHadele(String hadele) {
		this.handle = hadele;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public int getStepId() {
		return stepId;
	}
	public void setStepId(int stepId) {
		this.stepId = stepId;
	}
	public int getBusinessCycle() {
		return businessCycle;
	}
	public void setBusinessCycle(int businessCycle) {
		this.businessCycle = businessCycle;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public String getSfc() {
		return sfc;
	}
	public void setSfc(String sfc) {
		this.sfc = sfc;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getAltetSequenceNumber() {
		return altetSequenceNumber;
	}
	public void setAltetSequenceNumber(int altetSequenceNumber) {
		this.altetSequenceNumber = altetSequenceNumber;
	}
	public String getTxAlertListInfoBO() {
		return txAlertListInfoBO;
	}
	public void setTxAlertListInfoBO(String txAlertListInfoBO) {
		this.txAlertListInfoBO = txAlertListInfoBO;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProcessDataBO() {
		return processDataBO;
	}
	public void setProcessDataBO(String processDataBO) {
		this.processDataBO = processDataBO;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public String getErpResourceBO() {
		return erpResourceBO;
	}
	public void setErpResourceBO(String erpResourceBO) {
		this.erpResourceBO = erpResourceBO;
	}
	public int getChannelId() {
		return channelId;
	}
	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}
	public int getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(int alertLevel) {
		this.alertLevel = alertLevel;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getUpLimit() {
		return upLimit;
	}
	public void setUpLimit(BigDecimal upLimit) {
		this.upLimit = upLimit;
	}
	public BigDecimal getLowLimit() {
		return lowLimit;
	}
	public void setLowLimit(BigDecimal lowLimit) {
		this.lowLimit = lowLimit;
	}
	public String getOriginalProcessDataBO() {
		return originalProcessDataBO;
	}
	public void setOriginalProcessDataBO(String originalProcessDataBO) {
		this.originalProcessDataBO = originalProcessDataBO;
	}
	public Timestamp getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(Timestamp createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getCreatedUser() {
		return createdUser;
	}
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}
	public Timestamp getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Timestamp modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

}
