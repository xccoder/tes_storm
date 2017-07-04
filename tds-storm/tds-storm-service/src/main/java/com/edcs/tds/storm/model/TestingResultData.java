package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

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
	
	private String handle;//TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>,<ALERT_SEQUENCE_NUMBER>
	private String site;
	private String remark;//流程编号
	private int stepId;//工步序号
	private int businessCycle;//业务循环号。
	private int cycle;//设备循环号
	private String sfc;//电芯号
	private String category;//类别（电流，电压。。。。）
	private int altetSequenceNumber;//序列值（同一个工步之间报警的序号）
	private String txAlertListInfoBO;//TX_ALERT_LIST_INFO.HANDLE  TxAlertListInfoBO:<SITE>,<ALERT_LIST_ID>
	private String status;//状态，new  、close、inprogress
	private String processDataBO;//MD_PROCESS_DATA.HANDLE  MdProcessInfoBO:<SITE>,<PROCESS_ID>,<REMARK>
	private Date timestamp;//记录报警时间（什么时候报警的）
	private String erpResourceBO;//ERP_RESOURCE.HANDLE   ErpResourceBO:<SITE>,<RESOURCE_ID>
	private int channelId;//通道号
	private String alertLevel;//报警级别
	private String description;//报警信息描述
	private BigDecimal upLimit;//报警上限
	private BigDecimal lowLimit;//报警下限
	private String originalProcessDataBO;//TX_ORIGINAL_PROCESS_DATA.HANDLE   TxOriginalProcessDataBO:<SITE>,<REMARK>,<SFC> ,<RESOURCE_ID>,<CHANNEL_ID>,<SEQUENCE_ID>
	private Timestamp createdDateTime;//创建时间
	private String createdUser;//创建用户
	private Date modifiedDateTime;//最后修改日期
	private String modifiedUser;//最后修改用户
	private String sequenceId;  //记录序号

	public String getSequenceId() {
		return sequenceId;
	}

	public void setSequenceId(String sequenceId) {
		this.sequenceId = sequenceId;
	}

	public String getHandle() {
		return handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
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
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
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
	public String getAlertLevel() {
		return alertLevel;
	}
	public void setAlertLevel(String alertLevel) {
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
	public Date getModifiedDateTime() {
		return modifiedDateTime;
	}
	public void setModifiedDateTime(Date modifiedDateTime) {
		this.modifiedDateTime = modifiedDateTime;
	}
	public String getModifiedUser() {
		return modifiedUser;
	}
	public void setModifiedUser(String modifiedUser) {
		this.modifiedUser = modifiedUser;
	}

}
