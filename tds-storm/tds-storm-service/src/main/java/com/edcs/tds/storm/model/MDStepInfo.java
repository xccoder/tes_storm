package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by CaiSL2 on 2017/6/29.
 */
public class MDStepInfo implements Serializable {

    private static final long serialVersionUID = -6709668054897110540L;
    private String handle;
    private String site;
    private String remark;//流程文件号
    private int stepId;//工步号
    private String stepName;//工步名称
    private String scriptCurrent;//电流判异脚本
    private String scriptVoltage;//电压判异脚本
    private String scriptTemperature;//温度判异脚本
    private String scriptTime;//时间判异脚本
    private String scriptCapacity;//容量判异脚本
    private String scriptEnergy;//能量判异脚本
    private boolean isCycleSignalStep;//是否循环标识
    private Double deltaVoltage;//电压差
    private Double svPower;//功率
    private Double svIR;//内阻
    private Double svVoltage;//电压
    private Double svCapacity;//容量
    private Double svCurrent;//电流
    private Double svStepEndCapacity;//工步截止容量
    private Double svStepEndVoltage;//工步截止电压
    private Double svStepEndCurrent;//工步截止电流
    private Double svStepEndTemperature;//工步截止温度
    private String scriptCurrenthash;//电流脚本hash
    private String scriptVoltagehash;//电压脚本hash
    private String scriptTemperaturehash;//温度脚本hash;
    private String scriptTimehash;//时间脚本hash
    private String scriptCapacityhash;//容量脚本Hash
    private String scriptEnergyhash;//能量脚本hash
    private String startStep;//起始工步
    private Date createDateTime;//创建日期
    private String createUser;//创建用户
    private Date modifiedDateTime;//最后修改日期
    private String modifeidUser;//最后修改用户
    private List<MDSubRule> mdSubRuleList;//抽取规则列表
    public MDStepInfo() {
    }

    public String getScriptCurrenthash() {
        return scriptCurrenthash;
    }

    public void setScriptCurrenthash(String scriptCurrenthash) {
        this.scriptCurrenthash = scriptCurrenthash;
    }

    public String getScriptVoltagehash() {
        return scriptVoltagehash;
    }

    public void setScriptVoltagehash(String scriptVoltagehash) {
        this.scriptVoltagehash = scriptVoltagehash;
    }

    public String getScriptTemperaturehash() {
        return scriptTemperaturehash;
    }

    public void setScriptTemperaturehash(String scriptTemperaturehash) {
        this.scriptTemperaturehash = scriptTemperaturehash;
    }

    public String getScriptTimehash() {
        return scriptTimehash;
    }

    public void setScriptTimehash(String scriptTimehash) {
        this.scriptTimehash = scriptTimehash;
    }

    public String getScriptCapacityhash() {
        return scriptCapacityhash;
    }

    public void setScriptCapacityhash(String scriptCapacityhash) {
        this.scriptCapacityhash = scriptCapacityhash;
    }

    public String getScriptEnergyhash() {
        return scriptEnergyhash;
    }

    public void setScriptEnergyhash(String scriptEnergyhash) {
        this.scriptEnergyhash = scriptEnergyhash;
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

    public String getModifeidUser() {
        return modifeidUser;
    }

    public void setModifeidUser(String modifeidUser) {
        this.modifeidUser = modifeidUser;
    }

    public Double getSvStepEndVoltage() {
        return svStepEndVoltage;
    }

    public void setSvStepEndVoltage(Double svStepEndVoltage) {
        this.svStepEndVoltage = svStepEndVoltage;
    }

    public Double getSvStepEndCurrent() {
        return svStepEndCurrent;
    }

    public void setSvStepEndCurrent(Double svStepEndCurrent) {
        this.svStepEndCurrent = svStepEndCurrent;
    }

    public List<MDSubRule> getMdSubRuleList() {
        return mdSubRuleList;
    }

    public void setMdSubRuleList(List<MDSubRule> mdSubRuleList) {
        this.mdSubRuleList = mdSubRuleList;
    }

    public String getScriptEnergy() {
        return scriptEnergy;
    }

    public void setScriptEnergy(String scriptEnergy) {
        this.scriptEnergy = scriptEnergy;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getScriptCurrent() {
        return scriptCurrent;
    }

    public void setScriptCurrent(String scriptCurrent) {
        this.scriptCurrent = scriptCurrent;
    }

    public String getScriptVoltage() {
        return scriptVoltage;
    }

    public void setScriptVoltage(String scriptVoltage) {
        this.scriptVoltage = scriptVoltage;
    }

    public String getScriptTemperature() {
        return scriptTemperature;
    }

    public void setScriptTemperature(String scriptTemperature) {
        this.scriptTemperature = scriptTemperature;
    }

    public String getScriptTime() {
        return scriptTime;
    }

    public void setScriptTime(String scriptTime) {
        this.scriptTime = scriptTime;
    }

    public String getScriptCapacity() {
        return scriptCapacity;
    }

    public void setScriptCapacity(String scriptCapacity) {
        this.scriptCapacity = scriptCapacity;
    }

    public boolean isCycleSignalStep() {
        return isCycleSignalStep;
    }

    public void setCycleSignalStep(boolean cycleSignalStep) {
        isCycleSignalStep = cycleSignalStep;
    }

    public Double getDeltaVoltage() {
        return deltaVoltage;
    }

    public void setDeltaVoltage(Double deltaVoltage) {
        this.deltaVoltage = deltaVoltage;
    }

    public Double getSvPower() {
        return svPower;
    }

    public void setSvPower(Double svPower) {
        this.svPower = svPower;
    }

    public Double getSvIR() {
        return svIR;
    }

    public void setSvIR(Double svIR) {
        this.svIR = svIR;
    }

    public Double getSvVoltage() {
        return svVoltage;
    }

    public void setSvVoltage(Double svVoltage) {
        this.svVoltage = svVoltage;
    }

    public Double getSvCapacity() {
        return svCapacity;
    }

    public void setSvCapacity(Double svCapacity) {
        this.svCapacity = svCapacity;
    }

    public Double getSvCurrent() {
        return svCurrent;
    }

    public void setSvCurrent(Double svCurrent) {
        this.svCurrent = svCurrent;
    }

    public Double getSvStepEndCapacity() {
        return svStepEndCapacity;
    }

    public void setSvStepEndCapacity(Double svStepEndCapacity) {
        this.svStepEndCapacity = svStepEndCapacity;
    }

    public Double getSvStepEndTemperature() {
        return svStepEndTemperature;
    }

    public void setSvStepEndTemperature(Double svStepEndTemperature) {
        this.svStepEndTemperature = svStepEndTemperature;
    }

    public String getStartStep() {
        return startStep;
    }

    public void setStartStep(String startStep) {
        this.startStep = startStep;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }
}
