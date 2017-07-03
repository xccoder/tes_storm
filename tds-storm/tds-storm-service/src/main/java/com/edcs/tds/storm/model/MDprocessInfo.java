package com.edcs.tds.storm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by CaiSL2 on 2017/6/29.
 */
public class MDprocessInfo implements Serializable {

    private static final long serialVersionUID = -8747661550591511712L;
    private String handle;
    private String processID;//流程号
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
    private Double svInitFixtureForce;//初始夹具力
    private Double svInitWeight;//初始重量
    private Double svInitVolume;//初始体积
    private Double svInitIR;//初始电阻
    private Double svInitOCV;//初始ocv
    private Double svInitThickness;//初始厚度
    private String daysOrCycle;//天数/循环
    private Double svLowerU;//电压下线
    private Double svUpperU;//电压上线
    private Double svTemperature;//温度
    private Double svTimeDuration;//测试时间
    private Double svChangeCurrent;//充电电流
    private Double svDischargeVoltage;//放电电流
    private Double svDischargeCUrrent;//放电电压
    private Double svChangeVoltage;//充电电压
    private Double svChangrPower;//充电功率
    private Double svDischargePower;//放电功率
    private int cycleNumIber;//循环数
    private Double soc;//soc
    private int pluseTimes;//脉冲次数
    private Double chargeMulti;//充电倍率
    private Double disChargeMutil;//放电倍率
    private String storeEndCOndition;//存储结束条件
    private String storeCrossCondition;//存储交叉条件
    private String cycleEndCondition;//循环结束条件
    private String cycleCrossCondition;//循环交叉条件
    private String simEndCondition;//工况结束条件
    private String simCrossCondition;//工况交叉条件
    private boolean isISODischarge;//是否恒压放电
    private String isoDischargeDuration;//恒压放电时长
    private String constantIRValue;//横阻值
    private String cycleTemperature;//循环温度
    private String storeTemperature;//存储温度
    private String txStatus;//流程状态
    private Date createDateTime;//创建日期
    private String createUser;//创建用户
    private Date modifiedDateTime;//最后修改时间
    private String modifiedUser;//最后修改用户
    private List<MDStepInfo> mdStepInfoList;//流程下所有工步
    private String rootRemark;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getTxStatus() {
        return txStatus;
    }

    public void setTxStatus(String txStatus) {
        this.txStatus = txStatus;
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

    public String getRootRemark() {
        return rootRemark;
    }

    public void setRootRemark(String rootRemark) {
        this.rootRemark = rootRemark;
    }

    public List<MDStepInfo> getMdStepInfoList() {
        return mdStepInfoList;
    }

    public void setMdStepInfoList(List<MDStepInfo> mdStepInfoList) {
        this.mdStepInfoList = mdStepInfoList;
    }


    public MDprocessInfo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getProcessID() {
        return processID;
    }

    public void setProcessID(String processID) {
        this.processID = processID;
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

    public Double getSvInitFixtureForce() {
        return svInitFixtureForce;
    }

    public void setSvInitFixtureForce(Double svInitFixtureForce) {
        this.svInitFixtureForce = svInitFixtureForce;
    }

    public Double getSvInitWeight() {
        return svInitWeight;
    }

    public void setSvInitWeight(Double svInitWeight) {
        this.svInitWeight = svInitWeight;
    }

    public Double getSvInitVolume() {
        return svInitVolume;
    }

    public void setSvInitVolume(Double svInitVolume) {
        this.svInitVolume = svInitVolume;
    }

    public Double getSvInitIR() {
        return svInitIR;
    }

    public void setSvInitIR(Double svInitIR) {
        this.svInitIR = svInitIR;
    }

    public Double getSvInitOCV() {
        return svInitOCV;
    }

    public void setSvInitOCV(Double svInitOCV) {
        this.svInitOCV = svInitOCV;
    }

    public Double getSvInitThickness() {
        return svInitThickness;
    }

    public void setSvInitThickness(Double svInitThickness) {
        this.svInitThickness = svInitThickness;
    }

    public String getDaysOrCycle() {
        return daysOrCycle;
    }

    public void setDaysOrCycle(String daysOrCycle) {
        this.daysOrCycle = daysOrCycle;
    }

    public Double getSvLowerU() {
        return svLowerU;
    }

    public void setSvLowerU(Double svLowerU) {
        this.svLowerU = svLowerU;
    }

    public Double getSvUpperU() {
        return svUpperU;
    }

    public void setSvUpperU(Double svUpperU) {
        this.svUpperU = svUpperU;
    }

    public Double getSvTemperature() {
        return svTemperature;
    }

    public void setSvTemperature(Double svTemperature) {
        this.svTemperature = svTemperature;
    }

    public Double getSvTimeDuration() {
        return svTimeDuration;
    }

    public void setSvTimeDuration(Double svTimeDuration) {
        this.svTimeDuration = svTimeDuration;
    }

    public Double getSvChangeCurrent() {
        return svChangeCurrent;
    }

    public void setSvChangeCurrent(Double svChangeCurrent) {
        this.svChangeCurrent = svChangeCurrent;
    }

    public Double getSvDischargeVoltage() {
        return svDischargeVoltage;
    }

    public void setSvDischargeVoltage(Double svDischargeVoltage) {
        this.svDischargeVoltage = svDischargeVoltage;
    }

    public Double getSvDischargeCUrrent() {
        return svDischargeCUrrent;
    }

    public void setSvDischargeCUrrent(Double svDischargeCUrrent) {
        this.svDischargeCUrrent = svDischargeCUrrent;
    }

    public Double getSvChangeVoltage() {
        return svChangeVoltage;
    }

    public void setSvChangeVoltage(Double svChangeVoltage) {
        this.svChangeVoltage = svChangeVoltage;
    }

    public Double getSvChangrPower() {
        return svChangrPower;
    }

    public void setSvChangrPower(Double svChangrPower) {
        this.svChangrPower = svChangrPower;
    }

    public Double getSvDischargePower() {
        return svDischargePower;
    }

    public void setSvDischargePower(Double svDischargePower) {
        this.svDischargePower = svDischargePower;
    }

    public int getCycleNumIber() {
        return cycleNumIber;
    }

    public void setCycleNumIber(int cycleNumIber) {
        this.cycleNumIber = cycleNumIber;
    }

    public Double getSoc() {
        return soc;
    }

    public void setSoc(Double soc) {
        this.soc = soc;
    }

    public int getPluseTimes() {
        return pluseTimes;
    }

    public void setPluseTimes(int pluseTimes) {
        this.pluseTimes = pluseTimes;
    }

    public Double getChargeMulti() {
        return chargeMulti;
    }

    public void setChargeMulti(Double chargeMulti) {
        this.chargeMulti = chargeMulti;
    }

    public Double getDisChargeMutil() {
        return disChargeMutil;
    }

    public void setDisChargeMutil(Double disChargeMutil) {
        this.disChargeMutil = disChargeMutil;
    }

    public String getStoreEndCOndition() {
        return storeEndCOndition;
    }

    public void setStoreEndCOndition(String storeEndCOndition) {
        this.storeEndCOndition = storeEndCOndition;
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

    public boolean ISODischarge() {
        return isISODischarge;
    }

    public void setISODischarge(boolean ISODischarge) {
        isISODischarge = ISODischarge;
    }

    public String getIsoDischargeDuration() {
        return isoDischargeDuration;
    }

    public void setIsoDischargeDuration(String isoDischargeDuration) {
        this.isoDischargeDuration = isoDischargeDuration;
    }

    public String getConstantIRValue() {
        return constantIRValue;
    }

    public void setConstantIRValue(String constantIRValue) {
        this.constantIRValue = constantIRValue;
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

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

}
