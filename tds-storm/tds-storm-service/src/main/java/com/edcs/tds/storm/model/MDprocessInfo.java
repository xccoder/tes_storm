package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by CaiSL2 on 2017/6/29.
 */
public class MDprocessInfo implements Serializable {

    private static final long serialVersionUID = -8747661550591511712L;
    private String handle;
    private String processId;//流程号
    private String site;//1000表示测试的是电芯
    private String remark;//流程文件标识号
    private String testRequestId;//测试申请单号
    private String groupName;//组别
    private String sfc;//电芯barcode
    private String engineer;//测试工程师
    private String packageId;
    private String project;//测试项目
    private String description;//描述
    private String svModel;//model
    private String svCapacityValue;//标称容量
    private String fixtureType;//夹具模型
    private BigDecimal svInitFixtureForce;//初始夹具力
    private BigDecimal svInitWeight;//初始重量
    private BigDecimal svInitVolume;//初始体积
    private BigDecimal svInitIr;//初始电阻
    private BigDecimal svInitOcv;//初始ocv
    private BigDecimal svInitThickness;//初始厚度
    private Double daysOrCycle;//天数/循环
    private BigDecimal svLowerU;//电压下线
    private BigDecimal svUpperU;//电压上线
    private BigDecimal svTemperature;//温度
    private BigDecimal svTimeDuration;//测试时间
    private BigDecimal svChargeCurrent;//充电电流
    private BigDecimal svDischargeVoltage;//放电电流
    private BigDecimal svDischargeCurrent;//放电电压
    private BigDecimal svChargeVoltage;//充电电压
    private BigDecimal svChargePower;//充电功率
    private BigDecimal svDischargePower;//放电功率
    private int cycleNumber;//循环数
    private BigDecimal soc;//soc
    private int pluseTimes;//脉冲次数
    private String chargeMulti;//充电倍率
    private String dischargeMulti;//放电倍率
    private String storeEndCondition;//存储结束条件
    private String storeCrossCondition;//存储交叉条件
    private String cycleEndCondition;//循环结束条件
    private String cycleCrossCondition;//循环交叉条件
    private String simEndCondition;//工况结束条件
    private String simCrossCondition;//工况交叉条件
    private boolean isIsoDischarge;//是否恒压放电
    private String isoDischargeDuration;//恒压放电时长
    private String constantIrValue;//横阻值
    private String cycleTemperature;//循环温度
    private String storeTemperature;//存储温度
    private String txStatus;//流程状态
    private Date createDateTime;//创建日期
    private String createUser;//创建用户
    private Date modifiedDateTime;//最后修改时间
    private String modifiedUser;//最后修改用户
    private List<MDStepInfo> mdStepInfoList;//流程下所有工步
    private List<MDSubRule> mdExtractionRuleList;
    private String rootRemark;

    @Override
    public String toString() {
        return "MDprocessInfo{" +
                "handle='" + handle + '\'' +
                ", processId='" + processId + '\'' +
                ", site='" + site + '\'' +
                ", remark='" + remark + '\'' +
                ", testRequestId='" + testRequestId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", sfc='" + sfc + '\'' +
                ", engineer='" + engineer + '\'' +
                ", packageId='" + packageId + '\'' +
                ", project='" + project + '\'' +
                ", description='" + description + '\'' +
                ", svModel='" + svModel + '\'' +
                ", svCapacityValue='" + svCapacityValue + '\'' +
                ", fixtureType='" + fixtureType + '\'' +
                ", svInitFixtureForce=" + svInitFixtureForce +
                ", svInitWeight=" + svInitWeight +
                ", svInitVolume=" + svInitVolume +
                ", svInitIr=" + svInitIr +
                ", svInitOcv=" + svInitOcv +
                ", svInitThickness=" + svInitThickness +
                ", daysOrCycle='" + daysOrCycle + '\'' +
                ", svLowerU=" + svLowerU +
                ", svUpperU=" + svUpperU +
                ", svTemperature=" + svTemperature +
                ", svTimeDuration=" + svTimeDuration +
                ", svChargeCurrent=" + svChargeCurrent +
                ", svDischargeVoltage=" + svDischargeVoltage +
                ", svDischargeCurrent=" + svDischargeCurrent +
                ", svChargeVoltage=" + svChargeVoltage +
                ", svChargePower=" + svChargePower +
                ", svDischargePower=" + svDischargePower +
                ", cycleNumber=" + cycleNumber +
                ", soc=" + soc +
                ", pluseTimes=" + pluseTimes +
                ", chargeMulti=" + chargeMulti +
                ", dischargeMulti=" + dischargeMulti +
                ", storeEndCondition='" + storeEndCondition + '\'' +
                ", storeCrossCondition='" + storeCrossCondition + '\'' +
                ", cycleEndCondition='" + cycleEndCondition + '\'' +
                ", cycleCrossCondition='" + cycleCrossCondition + '\'' +
                ", simEndCondition='" + simEndCondition + '\'' +
                ", simCrossCondition='" + simCrossCondition + '\'' +
                ", isIsoDischarge=" + isIsoDischarge +
                ", isoDischargeDuration='" + isoDischargeDuration + '\'' +
                ", constantIrValue='" + constantIrValue + '\'' +
                ", cycleTemperature='" + cycleTemperature + '\'' +
                ", storeTemperature='" + storeTemperature + '\'' +
                ", txStatus='" + txStatus + '\'' +
                ", createDateTime=" + createDateTime +
                ", createUser='" + createUser + '\'' +
                ", modifiedDateTime=" + modifiedDateTime +
                ", modifiedUser='" + modifiedUser + '\'' +
                ", mdStepInfoList=" + mdStepInfoList +
                ", mdExtractionRuleList=" + mdExtractionRuleList +
                ", rootRemark='" + rootRemark + '\'' +
                '}';
    }

    public MDprocessInfo() {
    }

    public List<MDSubRule> getMdExtractionRuleList() {
        return mdExtractionRuleList;
    }

    public void setMdExtractionRuleList(List<MDSubRule> mdExtractionRuleList) {
        this.mdExtractionRuleList = mdExtractionRuleList;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
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

    public String getTestRequestId() {
        return testRequestId;
    }

    public void setTestRequestId(String testRequestId) {
        this.testRequestId = testRequestId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSfc() {
        return sfc;
    }

    public void setSfc(String sfc) {
        this.sfc = sfc;
    }

    public String getEngineer() {
        return engineer;
    }

    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSvModel() {
        return svModel;
    }

    public void setSvModel(String svModel) {
        this.svModel = svModel;
    }

    public String getSvCapacityValue() {
        return svCapacityValue;
    }

    public void setSvCapacityValue(String svCapacityValue) {
        this.svCapacityValue = svCapacityValue;
    }

    public String getFixtureType() {
        return fixtureType;
    }

    public void setFixtureType(String fixtureType) {
        this.fixtureType = fixtureType;
    }

    public BigDecimal getSvInitFixtureForce() {
        return svInitFixtureForce;
    }

    public void setSvInitFixtureForce(BigDecimal svInitFixtureForce) {
        this.svInitFixtureForce = svInitFixtureForce;
    }

    public BigDecimal getSvInitWeight() {
        return svInitWeight;
    }

    public void setSvInitWeight(BigDecimal svInitWeight) {
        this.svInitWeight = svInitWeight;
    }

    public BigDecimal getSvInitVolume() {
        return svInitVolume;
    }

    public void setSvInitVolume(BigDecimal svInitVolume) {
        this.svInitVolume = svInitVolume;
    }

    public BigDecimal getSvInitIr() {
        return svInitIr;
    }

    public void setSvInitIr(BigDecimal svInitIr) {
        this.svInitIr = svInitIr;
    }

    public BigDecimal getSvInitOcv() {
        return svInitOcv;
    }

    public void setSvInitOcv(BigDecimal svInitOcv) {
        this.svInitOcv = svInitOcv;
    }

    public BigDecimal getSvInitThickness() {
        return svInitThickness;
    }

    public void setSvInitThickness(BigDecimal svInitThickness) {
        this.svInitThickness = svInitThickness;
    }



    public BigDecimal getSvLowerU() {
        return svLowerU;
    }

    public void setSvLowerU(BigDecimal svLowerU) {
        this.svLowerU = svLowerU;
    }

    public BigDecimal getSvUpperU() {
        return svUpperU;
    }

    public void setSvUpperU(BigDecimal svUpperU) {
        this.svUpperU = svUpperU;
    }

    public BigDecimal getSvTemperature() {
        return svTemperature;
    }

    public void setSvTemperature(BigDecimal svTemperature) {
        this.svTemperature = svTemperature;
    }

    public BigDecimal getSvTimeDuration() {
        return svTimeDuration;
    }

    public void setSvTimeDuration(BigDecimal svTimeDuration) {
        this.svTimeDuration = svTimeDuration;
    }

    public BigDecimal getSvChargeCurrent() {
        return svChargeCurrent;
    }

    public void setSvChargeCurrent(BigDecimal svChargeCurrent) {
        this.svChargeCurrent = svChargeCurrent;
    }

    public BigDecimal getSvDischargeVoltage() {
        return svDischargeVoltage;
    }

    public void setSvDischargeVoltage(BigDecimal svDischargeVoltage) {
        this.svDischargeVoltage = svDischargeVoltage;
    }

    public BigDecimal getSvDischargeCurrent() {
        return svDischargeCurrent;
    }

    public void setSvDischargeCurrent(BigDecimal svDischargeCurrent) {
        this.svDischargeCurrent = svDischargeCurrent;
    }

    public BigDecimal getSvChargeVoltage() {
        return svChargeVoltage;
    }

    public void setSvChargeVoltage(BigDecimal svChargeVoltage) {
        this.svChargeVoltage = svChargeVoltage;
    }

    public BigDecimal getSvChargePower() {
        return svChargePower;
    }

    public void setSvChargePower(BigDecimal svChargePower) {
        this.svChargePower = svChargePower;
    }

    public BigDecimal getSvDischargePower() {
        return svDischargePower;
    }

    public void setSvDischargePower(BigDecimal svDischargePower) {
        this.svDischargePower = svDischargePower;
    }

    public int getCycleNumber() {
        return cycleNumber;
    }

    public void setCycleNumber(int cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    public BigDecimal getSoc() {
        return soc;
    }

    public void setSoc(BigDecimal soc) {
        this.soc = soc;
    }

    public int getPluseTimes() {
        return pluseTimes;
    }

    public void setPluseTimes(int pluseTimes) {
        this.pluseTimes = pluseTimes;
    }

    public Double getDaysOrCycle() {
        return daysOrCycle;
    }

    public void setDaysOrCycle(Double daysOrCycle) {
        this.daysOrCycle = daysOrCycle;
    }

    public String getChargeMulti() {
        return chargeMulti;
    }

    public void setChargeMulti(String chargeMulti) {
        this.chargeMulti = chargeMulti;
    }

    public String getDischargeMulti() {
        return dischargeMulti;
    }

    public void setDischargeMulti(String dischargeMulti) {
        this.dischargeMulti = dischargeMulti;
    }

    public String getStoreEndCondition() {
        return storeEndCondition;
    }

    public void setStoreEndCondition(String storeEndCondition) {
        this.storeEndCondition = storeEndCondition;
    }

    public String getStoreCrossCondition() {
        return storeCrossCondition;
    }

    public void setStoreCrossCondition(String storeCrossCondition) {
        this.storeCrossCondition = storeCrossCondition;
    }

    public String getCycleEndCondition() {
        return cycleEndCondition;
    }

    public void setCycleEndCondition(String cycleEndCondition) {
        this.cycleEndCondition = cycleEndCondition;
    }

    public String getCycleCrossCondition() {
        return cycleCrossCondition;
    }

    public void setCycleCrossCondition(String cycleCrossCondition) {
        this.cycleCrossCondition = cycleCrossCondition;
    }

    public String getSimEndCondition() {
        return simEndCondition;
    }

    public void setSimEndCondition(String simEndCondition) {
        this.simEndCondition = simEndCondition;
    }

    public String getSimCrossCondition() {
        return simCrossCondition;
    }

    public void setSimCrossCondition(String simCrossCondition) {
        this.simCrossCondition = simCrossCondition;
    }

    public boolean isoDischarge() {
        return isIsoDischarge;
    }

    public void setIsoDischarge(boolean isoDischarge) {
        isIsoDischarge = isoDischarge;
    }

    public String getIsoDischargeDuration() {
        return isoDischargeDuration;
    }

    public void setIsoDischargeDuration(String isoDischargeDuration) {
        this.isoDischargeDuration = isoDischargeDuration;
    }

    public String getConstantIrValue() {
        return constantIrValue;
    }

    public void setConstantIrValue(String constantIrValue) {
        this.constantIrValue = constantIrValue;
    }

    public String getCycleTemperature() {
        return cycleTemperature;
    }

    public void setCycleTemperature(String cycleTemperature) {
        this.cycleTemperature = cycleTemperature;
    }

    public String getStoreTemperature() {
        return storeTemperature;
    }

    public void setStoreTemperature(String storeTemperature) {
        this.storeTemperature = storeTemperature;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
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

    public List<MDStepInfo> getMdStepInfoList() {
        return mdStepInfoList;
    }

    public void setMdStepInfoList(List<MDStepInfo> mdStepInfoList) {
        this.mdStepInfoList = mdStepInfoList;
    }

    public String getRootRemark() {
        return rootRemark;
    }

    public void setRootRemark(String rootRemark) {
        this.rootRemark = rootRemark;
    }
}
