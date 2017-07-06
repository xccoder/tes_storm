package com.edcs.tds.common.model;

import java.io.Serializable;
import java.util.Date;

public class RuleConfig implements Serializable {

	private static final long serialVersionUID = -9038328210279400521L;

	private Long id;
	private String ruleName;
	private String stepId;
	private String stepName;
	private Long ruleGroup;
	private String ruleDesc;
	private Integer ruleState;
	private Long opration;
	private Date createtime;
	private Date updatetime;
	private String ruleScript;
	private String hashcode;

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public Long getRuleGroup() {
		return ruleGroup;
	}

	public void setRuleGroup(Long ruleGroup) {
		this.ruleGroup = ruleGroup;
	}

	public String getRuleDesc() {
		return ruleDesc;
	}

	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	public Integer getRuleState() {
		return ruleState;
	}

	public void setRuleState(Integer ruleState) {
		this.ruleState = ruleState;
	}

	public Long getOpration() {
		return opration;
	}

	public void setOpration(Long opration) {
		this.opration = opration;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public String getRuleScript() {
		return ruleScript;
	}

	public void setRuleScript(String ruleScript) {
		this.ruleScript = ruleScript;
	}

	public String getHashcode() {
		return hashcode;
	}

	public void setHashcode(String hashcode) {
		this.hashcode = hashcode;
	}

	public RuleConfig(Long id, String ruleName, Long ruleGroup, String ruleDesc, Integer ruleState, Long opration,
			Date createtime, Date updatetime, String ruleScript, String hashcode) {
		super();
		this.id = id;
		this.ruleName = ruleName;
		this.ruleGroup = ruleGroup;
		this.ruleDesc = ruleDesc;
		this.ruleState = ruleState;
		this.opration = opration;
		this.createtime = createtime;
		this.updatetime = updatetime;
		this.ruleScript = ruleScript;
		this.hashcode = hashcode;
	}

	public RuleConfig() {
		super();
	}

	@Override
	public String toString() {
		return "RuleConfig [id=" + id + ", ruleName=" + ruleName + ", ruleGroup=" + ruleGroup + ", ruleDesc=" + ruleDesc
				+ ", ruleState=" + ruleState + ", opration=" + opration + ", createtime=" + createtime + ", updatetime="
				+ updatetime + ", ruleScript=" + ruleScript + ", hashcode=" + hashcode + "]";
	}

}