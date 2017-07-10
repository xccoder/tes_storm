package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
    private BigDecimal deltaVoltage;//电压差
    private BigDecimal svPower;//功率
    private BigDecimal svIr;//内阻
    private BigDecimal svVoltage;//电压
    private BigDecimal svCapacity;//容量
    private BigDecimal svCurrent;//电流
    private BigDecimal svEnergy;//能量
    private BigDecimal svTemperature;//温度
    private BigDecimal svTime;
    private int cycleCount;//循环数
    private String conditionType;//循环条件
    private String conditionOperationalCharacter;//条件操作符
    private BigDecimal conditionValue;//条件值
    private String gotoStep;//跳转工步
    private String startStep;//起始工步
    private BigDecimal startSoc;//开始SOC
    private BigDecimal socIncrement;//SOC增量
    private BigDecimal endSoc;//结束SOC

    private BigDecimal svStepEndCapacity;//工步截止容量
    private BigDecimal svStepEndVoltage;//工步截止电压
    private BigDecimal svStepEndCurrent;//工步截止电流
    private BigDecimal svStepEndTemperature;//工步截止温度

    private String scriptCurrentHash;//电流脚本hash
    private String scriptVoltageHash;//电压脚本hash
    private String scriptTemperatureHash;//温度脚本hash;
    private String scriptTimeHash;//时间脚本hash
    private String scriptCapacityHash;//容量脚本Hash
    private String scriptEnergyHash;//能量脚本hash

    private Date createDateTime;//创建日期
    private String createUser;//创建用户
    private Date modifiedDateTime;//最后修改日期
    private String modifeidUser;//最后修改用户


    public MDStepInfo() {
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

    public String getScriptEnergy() {
        return scriptEnergy;
    }

    public void setScriptEnergy(String scriptEnergy) {
        this.scriptEnergy = scriptEnergy;
    }

    public boolean isCycleSignalStep() {
        return isCycleSignalStep;
    }

    public void setCycleSignalStep(boolean cycleSignalStep) {
        isCycleSignalStep = cycleSignalStep;
    }

    public BigDecimal getDeltaVoltage() {
        return deltaVoltage;
    }

    public void setDeltaVoltage(BigDecimal deltaVoltage) {
        this.deltaVoltage = deltaVoltage;
    }

    public BigDecimal getSvPower() {
        return svPower;
    }

    public void setSvPower(BigDecimal svPower) {
        this.svPower = svPower;
    }

    public BigDecimal getSvIr() {
        return svIr;
    }

    public void setSvIr(BigDecimal svIr) {
        this.svIr = svIr;
    }

    public BigDecimal getSvVoltage() {
        return svVoltage;
    }

    public void setSvVoltage(BigDecimal svVoltage) {
        this.svVoltage = svVoltage;
    }

    public BigDecimal getSvCapacity() {
        return svCapacity;
    }

    public void setSvCapacity(BigDecimal svCapacity) {
        this.svCapacity = svCapacity;
    }

    public BigDecimal getSvCurrent() {
        return svCurrent;
    }

    public void setSvCurrent(BigDecimal svCurrent) {
        this.svCurrent = svCurrent;
    }

    public BigDecimal getSvEnergy() {
        return svEnergy;
    }

    public void setSvEnergy(BigDecimal svEnergy) {
        this.svEnergy = svEnergy;
    }

    public BigDecimal getSvTemperature() {
        return svTemperature;
    }

    public void setSvTemperature(BigDecimal svTemperature) {
        this.svTemperature = svTemperature;
    }

    public int getCycleCount() {
        return cycleCount;
    }

    public void setCycleCount(int cycleCount) {
        this.cycleCount = cycleCount;
    }

    public String getConditionType() {
        return conditionType;
    }

    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    public String getConditionOperationalCharacter() {
        return conditionOperationalCharacter;
    }

    public void setConditionOperationalCharacter(String conditionOperationalCharacter) {
        this.conditionOperationalCharacter = conditionOperationalCharacter;
    }

    public BigDecimal getSvTime() {
        return svTime;
    }

    public void setSvTime(BigDecimal svTime) {
        this.svTime = svTime;
    }

    public BigDecimal getConditionValue() {
        return conditionValue;
    }

    public void setConditionValue(BigDecimal conditionValue) {
        this.conditionValue = conditionValue;
    }

    public void setScriptEnergyHash(String scriptEnergyHash) {
        this.scriptEnergyHash = scriptEnergyHash;
    }

    public String getGotoStep() {
        return gotoStep;
    }

    public void setGotoStep(String gotoStep) {
        this.gotoStep = gotoStep;
    }

    public BigDecimal getStartSoc() {
        return startSoc;
    }

    public void setStartSoc(BigDecimal startSoc) {
        this.startSoc = startSoc;
    }

    public BigDecimal getSocIncrement() {
        return socIncrement;
    }

    public void setSocIncrement(BigDecimal socIncrement) {
        this.socIncrement = socIncrement;
    }

    public BigDecimal getEndSoc() {
        return endSoc;
    }

    public void setEndSoc(BigDecimal endSoc) {
        this.endSoc = endSoc;
    }

    public BigDecimal getSvStepEndCapacity() {
        return svStepEndCapacity;
    }

    public void setSvStepEndCapacity(BigDecimal svStepEndCapacity) {
        this.svStepEndCapacity = svStepEndCapacity;
    }

    public BigDecimal getSvStepEndVoltage() {
        return svStepEndVoltage;
    }

    public void setSvStepEndVoltage(BigDecimal svStepEndVoltage) {
        this.svStepEndVoltage = svStepEndVoltage;
    }

    public BigDecimal getSvStepEndCurrent() {
        return svStepEndCurrent;
    }

    public void setSvStepEndCurrent(BigDecimal svStepEndCurrent) {
        this.svStepEndCurrent = svStepEndCurrent;
    }

    public BigDecimal getSvStepEndTemperature() {
        return svStepEndTemperature;
    }

    public void setSvStepEndTemperature(BigDecimal svStepEndTemperature) {
        this.svStepEndTemperature = svStepEndTemperature;
    }

    public String getScriptCurrentHash() {
        return scriptCurrentHash;
    }

    public void setScriptCurrentHash(String scriptCurrentHash) {
        this.scriptCurrentHash = scriptCurrentHash;
    }

    public String getScriptVoltageHash() {
        return scriptVoltageHash;
    }

    public void setScriptVoltageHash(String scriptVoltageHash) {
        this.scriptVoltageHash = scriptVoltageHash;
    }

    public String getScriptTemperatureHash() {
        return scriptTemperatureHash;
    }

    public void setScriptTemperatureHash(String scriptTemperatureHash) {
        this.scriptTemperatureHash = scriptTemperatureHash;
    }

    public String getScriptTimeHash() {
        return scriptTimeHash;
    }

    public void setScriptTimeHash(String scriptTimeHash) {
        this.scriptTimeHash = scriptTimeHash;
    }

    public String getScriptCapacityHash() {
        return scriptCapacityHash;
    }

    public void setScriptCapacityHash(String scriptCapacityHash) {
        this.scriptCapacityHash = scriptCapacityHash;
    }

    public String getScriptEnergyHash() {
        return scriptEnergyHash;
    }

    public void setScriptEnergyhash(String scriptEnergyHash) {
        this.scriptEnergyHash = scriptEnergyHash;
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
}