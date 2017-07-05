package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by CaiSL2 on 2017/7/3.
 */

public class MDSubRule implements Serializable {

    private static final long serialVersionUID = -8597018568185680161L;
    private String handle;
    private String site;
    private String remark;
    private String extractionTag;
    private int stepId;
    private String conditionKey;
    private String conditionValue;
    private String targetProcessDataFiel;
    private String alias;
    private String calcScript;
    private String calcResult;
    private Date createDateTime;
    private String createUser;
    private Date modifiedDateTime;
    private String modifiedUser;

    public MDSubRule() {
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

    public String getExtractionTag() {
        return extractionTag;
    }

    public void setExtractionTag(String extractionTag) {
        this.extractionTag = extractionTag;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getConditionKey() {
        return conditionKey;
    }

    public void setConditionKey(String conditionKey) {
        this.conditionKey = conditionKey;
    }

    public String getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(String conditionValue) {
        this.conditionValue = conditionValue;
    }

    public String getTargetProcessDataFiel() {
        return targetProcessDataFiel;
    }

    public void setTargetProcessDataFiel(String targetProcessDataFiel) {
        this.targetProcessDataFiel = targetProcessDataFiel;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCalcScript() {
        return calcScript;
    }

    public void setCalcScript(String calcScript) {
        this.calcScript = calcScript;
    }

    public String getCalcResult() {
        return calcResult;
    }

    public void setCalcResult(String calcResult) {
        this.calcResult = calcResult;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
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
