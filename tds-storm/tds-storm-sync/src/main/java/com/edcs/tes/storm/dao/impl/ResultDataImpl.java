package com.edcs.tes.storm.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.List;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.model.TestingSubChannel;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.IResultData;
import com.edcs.tes.storm.extract.ExtractData;
import com.edcs.tes.storm.sync.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by CaiSL2 on 2017/7/4.
 */
public class ResultDataImpl implements IResultData {
    private DBHelperUtils db = new DBHelperUtils();
    private final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
    public ResultDataImpl(DBHelperUtils db) {
        this.db = db;
    }

    public ResultDataImpl() {
    }

    private boolean curr = false;//判断电流是否异常
    private boolean volt = false;//判断电压是否异常
    private boolean time = false;//判断时间是否异常
    private boolean capa = false;//判断容量是否异常
    private boolean temp = false;//判断温度是否异常
    private boolean processDataAlert = false;//判断整条数据是否有一个或多个异常

    private String remark = null;
    private String site = null;
    private String sfc = null;
    private int channelId;

    private Date createDateTime;
    private String createUser = null;
    private Date modifiedDateTime;
    private String modifiedUser = null;

    private int sequenceId;
    private int businessCycle;
    private int cycle;
    private String procehandle = null; //整条测试数据的handle
    private BigDecimal pvChargeCapacity;
    private BigDecimal pvChargeEnergy;
    private BigDecimal pvCurrent;
    private int pvDataFlag;
    private BigDecimal pvDischargeCapacity;
    private BigDecimal pvDischargeEnergy;
    private BigDecimal pvIr;
    private BigDecimal pvTemperature;
    private BigDecimal pvVoltage;
    private int pvWorkType;
    private String resourceId = null;
    private int stepId;
    private String stepName = null;
    private List<TestingSubChannel> subChannels = null;
    private String subchannel1 = null;
    private String subchannel2 = null;
    private String subchannel3 = null;
    private String subchannel4 = null;
    private String subchannel5 = null;
    private String subchannel6 = null;

    private BigDecimal svIcRange;
    private BigDecimal svIvRange;
    private BigDecimal testTimeDuration;
    private Date testingmesstimestamp;
    private int stepLogicNumber;
    private String isContainMainData;

    @Override
    public boolean insertResultData(List<TestingResultData> testingResultDatas) throws SQLException {
        String category;//场景
        int alertSquenceNumber;
        String AlertListInfohandle = null;
        String status = null;
        String processInfoBo;
        Date timestamp;
        String erpResourceBo;
        int alertLevel = 0;
        String description;
        BigDecimal upLimit;
        BigDecimal lowLimit;
        String originalProBo;
        String alertHandle;//每一个场景报警数据的handle
        String alertListId = null;
        //辅助通道
        String subHandle;
        int subChannelId;//辅助通道id，从辅助通道name上截取
        int subSequenceId;
        int subCycle;
        int subStepId;
        BigDecimal subTestTimeDuration;
        BigDecimal subVolt;
        BigDecimal subCurr;
        BigDecimal subIr;
        BigDecimal subTemp;
        BigDecimal subChargeCapacity;
        BigDecimal subDisChargeCapacity;
        BigDecimal subChargeEnergy;
        BigDecimal subDisChargeEnergy;
        Date subTimestamp;
        int subDataFlag;
        int subWorkType;
        //zip
        String zipHandle = null;

        Connection conn = null;
        PreparedStatement pst = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        PreparedStatement pst4 = null;

        try {
            conn = db.getConnection();

            conn.setAutoCommit(false);//
            //savePoint = conn.setSavepoint("point1");
            String sql = "insert into TX_ALERT_INFO(HANDLE,SITE,REMARK,SFC,CATEGORY,ALERT_SEQUENCE_NUMBER,TX_ALERT_LIST_INFO_BO,STATUS,PROCESS_INFO_BO,TIMESTAMP,ERP_RESOURCE_BO,CHANNEL_ID,ALERT_LEVEL,DESCRIPTION,UP_LIMIT,LOW_LIMIT,ORIGINAL_PROCESS_DATA_BO,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst = conn.prepareStatement(sql);
            for (TestingResultData testingResultData : testingResultDatas) {
                alertHandle = testingResultData.getHandle();//TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>,<sequencednumber>
                alertListId = testingResultData.getTestingMessage().getRemark() + "," + testingResultData.getTestingMessage().getSequenceId();//alertlistinfo里的字段
                AlertListInfohandle = "TxAlertListInfoBO:" + testingResultData.getSite() + "," + alertListId;//TxAlertListInfoBO:<SITE>,<ALERT_LIST_ID>
                remark = testingResultData.getTestingMessage().getRemark();
                site = testingResultData.getSite();
                sfc = testingResultData.getTestingMessage().getSfc();
                category = testingResultData.getCategory();//场景
                alertSquenceNumber = testingResultData.getAltetSequenceNumber();
                //txAlertListInfoBo = testingResultData.getTxAlertListInfoBO();
                status = testingResultData.getStatus();
                processInfoBo = testingResultData.getProcessDataBO();//流程主数据的handle

                timestamp = testingResultData.getTimestamp();
                erpResourceBo = testingResultData.getErpResourceBO();
                channelId = testingResultData.getTestingMessage().getChannelId();
                alertLevel = testingResultData.getAlertLevel();
                description = testingResultData.getDescription();
                upLimit = testingResultData.getUpLimit();
                lowLimit = testingResultData.getLowLimit();
                originalProBo = testingResultData.getOriginalProcessDataBO();
                createDateTime = testingResultData.getCreatedDateTime();
                createUser = testingResultData.getCreatedUser();
                modifiedDateTime = testingResultData.getModifiedDateTime();
                modifiedUser = testingResultData.getModifiedUser();
                sequenceId = testingResultData.getTestingMessage().getSequenceId();
                pvDataFlag = testingResultData.getTestingMessage().getPvDataFlag();
                businessCycle = testingResultData.getTestingMessage().getBusinessCycle();
                cycle = testingResultData.getTestingMessage().getCycle();
                procehandle = testingResultData.getOriginalProcessDataBO();
                System.out.println(procehandle + "procehandleprocehandleprocehandleprocehandle");
                pvChargeCapacity = testingResultData.getTestingMessage().getPvChargeCapacity();
                pvChargeEnergy = testingResultData.getTestingMessage().getPvChargeEnergy();
                pvCurrent = testingResultData.getTestingMessage().getPvCurrent();
                pvDataFlag = testingResultData.getTestingMessage().getPvDataFlag();
                pvDischargeCapacity = testingResultData.getTestingMessage().getPvDischargeCapacity();
                pvDischargeEnergy = testingResultData.getTestingMessage().getPvDischargeEnergy();
                pvIr = testingResultData.getTestingMessage().getPvIr();
                pvTemperature = testingResultData.getTestingMessage().getPvTemperature();
                pvVoltage = testingResultData.getTestingMessage().getPvVoltage();
                pvWorkType = testingResultData.getTestingMessage().getPvWorkType();
                resourceId = testingResultData.getTestingMessage().getResourceId();//设备号
                stepId = testingResultData.getTestingMessage().getStepId();
                stepName = testingResultData.getTestingMessage().getStepName();
                subChannels = testingResultData.getTestingMessage().getSubChannel();
                stepLogicNumber = testingResultData.getTestingMessage().getStepLogicNumber();
                svIcRange = testingResultData.getTestingMessage().getSvIcRange();
                svIvRange = testingResultData.getTestingMessage().getSvIvRange();
                testTimeDuration = testingResultData.getTestingMessage().getTestTimeDuration();
                testingmesstimestamp = testingResultData.getTestingMessage().getTimestamp();
                isContainMainData = testingResultData.getIsContainMainData();//判断是否匹配上主数据
                System.out.println("isContainMainData" + isContainMainData);
                zipHandle = "TechZipStatusBO:" + testingResultData.getSite() + "," + testingResultData.getTestingMessage().getRemark() + "," + testingResultData.getTestingMessage().getBusinessCycle() + "," + testingResultData.getTestingMessage().getStepId();
                if (alertLevel != 0 && category != null) {
                    switch (category) {
                        case "current":
                            curr = true;
                            processDataAlert = true;
                            break;
                        case "voltage":
                            volt = true;
                            processDataAlert = true;
                            break;
                        case "time":
                            time = true;
                            processDataAlert = true;
                            break;
                        case "capacity":
                            capa = true;
                            processDataAlert = true;
                            break;
                        case "temperature":
                            temp = true;
                            processDataAlert = true;
                            break;
                    }
                    pst.setString(1, alertHandle);
                    pst.setString(2, site);
                    pst.setString(3, remark);
                    pst.setString(4, sfc);
                    pst.setString(5, category);
                    pst.setInt(6, alertSquenceNumber);
                    pst.setString(7, AlertListInfohandle);
                    pst.setString(8, status);
                    pst.setString(9, processInfoBo); //备注：流程主数据的handle
                    pst.setObject(10, timestamp);             //注意
                    pst.setString(11, erpResourceBo);
                    pst.setInt(12, channelId);
                    pst.setInt(13, alertLevel);                         //更改类型
                    pst.setString(14, description);
                    pst.setBigDecimal(15, upLimit);
                    pst.setBigDecimal(16, lowLimit);
                    pst.setString(17, originalProBo);
                    pst.setObject(18, timestamp);             //注意
                    pst.setString(19, "HJHJ");
                    pst.setObject(20, timestamp);                //注意
                    pst.setString(21, "NMCX");
                    pst.addBatch();
                    pst.executeBatch();
                }
            }

            //第二张表
            String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA(HANDLE,TX_ORIGINAL_PROCESS_DATA_BO,SUB_CHANNEL_ID,SITE,REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,CYCLE,STEP_ID,TEST_TIME_DURATION,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,TIMESTAMP,DATA_FLAG,WORK_TYPE,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            pst1 = conn.prepareStatement(subsql);//插入子通道表
            if (subChannels != null) {
                for (TestingSubChannel testingSubChannel : subChannels) {
                    switch (testingSubChannel.getSubChannelName()) {
                        case "pvSubChannelData1":
                            subchannel1 = JsonUtils.toJson(testingSubChannel);
                            break;
                        case "pvSubChannelData2":
                            subchannel2 = JsonUtils.toJson(testingSubChannel);
                            break;
                        case "pvSubChannelData3":
                            subchannel3 = JsonUtils.toJson(testingSubChannel);
                            break;
                        case "pvSubChannelData4":
                            subchannel4 = JsonUtils.toJson(testingSubChannel);
                            break;
                        case "pvSubChannelData5":
                            subchannel5 = JsonUtils.toJson(testingSubChannel);
                            break;
                        case "pvSubChannelData6":
                            subchannel6 = JsonUtils.toJson(testingSubChannel);
                            break;
                    }
                    subHandle = "TxOriginalSubChannelDataBO:" + procehandle +","+ Integer.valueOf(testingSubChannel.getSubChannelName().substring(testingSubChannel.getSubChannelName().length() - 1));//:< TX_ORIGINAL_PROCESS_DATA_BO>,<SUB_CHANNEL_ID>
                    subChannelId = Integer.valueOf(testingSubChannel.getSubChannelName().substring(testingSubChannel.getSubChannelName().length() - 1));
                    subSequenceId = testingSubChannel.getSequenceId();
                    subCycle = testingSubChannel.getCycle();
                    subStepId = testingSubChannel.getStepId();
                    subTestTimeDuration = testingSubChannel.getTestTimeDuration();
                    subVolt = testingSubChannel.getVoltage();
                    subCurr = testingSubChannel.getCurrent();
                    subIr = testingSubChannel.getIr();
                    subTemp = testingSubChannel.getTemperature();
                    subChargeCapacity = testingSubChannel.getChargeCapacity();
                    subDisChargeCapacity = testingSubChannel.getDischargeCapacity();
                    subChargeEnergy = testingSubChannel.getChargeEnergy();
                    subDisChargeEnergy = testingSubChannel.getDischargeEnergy();
                    subTimestamp = testingSubChannel.getTimestamp();
                    subDataFlag = testingSubChannel.getDataFlag();
                    subWorkType = testingSubChannel.getWorkType();

                    pst1.setString(1, subHandle);
                    pst1.setString(2, procehandle);
                    pst1.setInt(3, subChannelId);
                    pst1.setString(4, site);
                    pst1.setString(5, remark);
                    pst1.setString(6, sfc);
                    pst1.setString(7, resourceId);
                    pst1.setInt(8, channelId);
                    pst1.setInt(9, subSequenceId);
                    pst1.setInt(10, subCycle);
                    pst1.setInt(11, subStepId);
                    pst1.setBigDecimal(12, subTestTimeDuration);
                    pst1.setBigDecimal(13, subVolt);
                    pst1.setBigDecimal(14, subCurr);
                    pst1.setBigDecimal(15, subIr);
                    pst1.setBigDecimal(16, subTemp);
                    pst1.setBigDecimal(17, subChargeCapacity);
                    pst1.setBigDecimal(18, subDisChargeCapacity);
                    pst1.setBigDecimal(19, subChargeEnergy);
                    pst1.setBigDecimal(20, subDisChargeEnergy);
                    pst1.setString(21, subTimestamp.toString());
                    pst1.setInt(22, subDataFlag);
                    pst1.setInt(23, subWorkType);
                    pst1.setObject(24, subTimestamp);
                    pst1.setString(25, "qqq");
                    pst1.setObject(26, subTimestamp);
                    pst1.setString(27, "www");
                    pst1.addBatch();//待定
                    pst1.executeBatch();
                    System.out.println("子通道表插入成功");
                }

            }
            //第三张表

            String procesql = "insert into TX_ORIGINAL_PROCESS_DATA(HANDLE,SITE,REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,CYCLE,STEP_ID,STEP_NAME,TEST_TIME_DURATION,TIMESTAMP,SV_IC_RANGE,SV_IV_RANGE,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,PV_SUB_CHANNEL_1,PV_SUB_CHANNEL_2,PV_SUB_CHANNEL_3,PV_SUB_CHANNEL_4,PV_SUB_CHANNEL_5,PV_SUB_CHANNEL_6,PV_DATA_FLAG,PV_WORK_TYPE,TX_IS_EXCEPTIONAL,TX_ALERT_CURRENT,TX_ALERT_VOLTAGE,TX_ALERT_TEMPERATURE,TX_ALERT_CAPACITY,TX_ALERT_DURATION,TX_ALERT_CATEGORY1,TX_ALERT_CATEGORY2,TX_ROOT_REMARK,ST_BUSINESS_CYCLE,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER,STEP_LOGIC_NUMBER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
            pst2 = conn.prepareStatement(procesql);//插入流程结果表
            pst2.setString(1, procehandle);
            pst2.setString(2, site);
            pst2.setString(3, remark);
            pst2.setString(4, sfc);
            pst2.setString(5, resourceId);
            pst2.setInt(6, channelId);
            pst2.setInt(7, sequenceId);
            pst2.setInt(8, cycle);
            pst2.setInt(9, stepId);
            pst2.setString(10, stepName);
            pst2.setBigDecimal(11, testTimeDuration);
            pst2.setString(12, testingmesstimestamp.toString());
            pst2.setBigDecimal(13, svIcRange);
            pst2.setBigDecimal(14, svIvRange);
            pst2.setBigDecimal(15, pvVoltage);
            pst2.setBigDecimal(16, pvCurrent);
            pst2.setBigDecimal(17, pvIr);
            pst2.setBigDecimal(18, pvTemperature);
            pst2.setBigDecimal(19, pvChargeCapacity);
            pst2.setBigDecimal(20, pvDischargeCapacity);
            pst2.setBigDecimal(21, pvChargeEnergy);
            pst2.setBigDecimal(22, pvDischargeEnergy);
            pst2.setString(23, subchannel1);
            pst2.setString(24, subchannel2);
            pst2.setString(25, subchannel3);
            pst2.setString(26, subchannel4);
            pst2.setString(27, subchannel5);
            pst2.setString(28, subchannel6);
            pst2.setInt(29, pvDataFlag);
            pst2.setInt(30, pvWorkType);
            pst2.setBoolean(31, processDataAlert);
            pst2.setBoolean(32, curr);
            pst2.setBoolean(33, volt);
            pst2.setBoolean(34, temp);
            pst2.setBoolean(35, capa);
            pst2.setBoolean(36, time);
            pst2.setString(37, "caty");
            pst2.setString(38, "caty");
            pst2.setString(39, "rootremark");
            pst2.setInt(40, businessCycle);
            pst2.setObject(41, createDateTime);
            pst2.setString(42, createUser);
            pst2.setObject(43, modifiedDateTime);
            pst2.setString(44, modifiedUser);
            pst2.setInt(45, stepLogicNumber);
            pst2.addBatch();//待定
            pst2.executeBatch();
            System.out.println("原始数据表插入成功");

            //插入第四张表
            if (alertLevel != 0 && isContainMainData.equals("1")) {
                String alertListsql = "insert into TX_ALERT_LIST_INFO(HANDLE,SITE,ALERT_LIST_ID,STATUS,FEEDBACK_I,FEEDBACK_U,FEEDBACK_T,FEEDBACK_C,FEEDBACK_D,COMMENTS,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pst3 = conn.prepareStatement(alertListsql);//插入报警列表
                pst3.setString(1, AlertListInfohandle);
                pst3.setString(2, site);
                pst3.setString(3, alertListId);
                pst3.setString(4, status);
                pst3.setString(5, "");//用户输入
                pst3.setString(6, "");
                pst3.setString(7, "");
                pst3.setString(8, "");
                pst3.setString(9, "");
                pst3.setString(10, "comments");
                pst3.setObject(11, createDateTime);
                pst3.setString(12, createUser);
                pst3.setObject(13, modifiedDateTime);
                pst3.setString(14, modifiedUser);
                pst3.addBatch();//待定
                pst3.executeBatch();
                System.out.println("=============");
            }
            //执行四表同时插入
            conn.commit();
            // pst.clearBatch();        //待定

            //用于确定工步结束，触发抽取
            if (pvDataFlag == 88 && isContainMainData.equals("1")) {//工步终止标识
                ExtractData extractData = new ExtractData(db);
                try {
                    String s = extractData.extractData(remark, businessCycle, stepId);
                    System.out.println("返回值原是：" + s);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //触发压缩表数据初始化
                String zipSql = "insert into TECH_ZIP_STATUS(HANDLE,SITE,REMARK,ST_BUSINESS_CYCLE,STEP_ID,I_STATUS,V_STATUS,T_STATUS,C_STATUS,E_STATUS,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
                pst4 = conn.prepareStatement(zipSql);
                pst4.setString(1, zipHandle);
                pst4.setString(2, site);
                pst4.setString(3, remark);
                pst4.setInt(4, businessCycle);
                pst4.setInt(5, stepId);
                pst4.setInt(6, 0);
                pst4.setInt(7, 0);
                pst4.setInt(8, 0);
                pst4.setInt(9, 0);
                pst4.setInt(10, 0);
                pst4.setObject(11, createDateTime);
                pst4.setString(12, createUser);
                pst4.setObject(13, modifiedDateTime);
                pst4.setString(14, modifiedUser);
                pst4.addBatch();
                pst4.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            //    conn.rollback(savePoint);            //回滚
            logger.error(e.getMessage());
            System.out.println("插入告警数据错误");
        } finally {
            db.close(null, pst);
            db.close(null, pst1);
            db.close(null, pst2);
            db.close(null, pst3);
            db.close(conn, pst4);
        }

        return true;
    }

    public static void main(String[] args) {
        IResultData iResultData = new ResultDataImpl();

    }
}
