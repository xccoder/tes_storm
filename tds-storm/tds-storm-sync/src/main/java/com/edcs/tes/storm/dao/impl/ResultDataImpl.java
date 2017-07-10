package com.edcs.tes.storm.dao.impl;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.model.TestingSubChannel;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.dao.IResultData;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by CaiSL2 on 2017/7/4.
 *
 */
public class ResultDataImpl implements IResultData {
    private DBHelperUtils db = new DBHelperUtils();
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
    private int stepLogicNumber ;

    @Override
    public boolean insertResultData(List<TestingResultData> testingResultDatas) throws SQLException {
        String category;//场景
        int alertSquenceNumber;
        String txAlertListInfoBo = null;//
        String AlertListInfohandle = null;
        String status = null;
        String processInfoBo;
        Date timestamp;
        String erpResourceBo;
        int alertLevel;
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


        Connection conn = null;
        PreparedStatement pst = null;

        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);
            for (TestingResultData testingResultData : testingResultDatas) {
                alertHandle = testingResultData.getHandle();//TxAlertInfoBO:<SITE>,<REMARK>,<SFC>,<CATEGORY>
                alertListId = testingResultData.getTestingMessage().getRemark()+","+testingResultData.getTestingMessage().getSequenceId();//alertlistinfo里的字段
                AlertListInfohandle = "TxAlertListInfoBO:"+testingResultData.getSite()+alertListId;//TxAlertListInfoBO:<SITE>,<ALERT_LIST_ID>
                remark = testingResultData.getTestingMessage().getRemark();
                site = testingResultData.getSite();
                sfc = testingResultData.getTestingMessage().getSfc();
                category = testingResultData.getCategory();//场景
                alertSquenceNumber = testingResultData.getAltetSequenceNumber();
                //txAlertListInfoBo = testingResultData.getTxAlertListInfoBO();
                status = testingResultData.getStatus();
                processInfoBo = testingResultData.getTxAlertListInfoBO();
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

                businessCycle = testingResultData.getTestingMessage().getBusinessCycle();
                cycle = testingResultData.getTestingMessage().getCycle();
                procehandle = testingResultData.getOriginalProcessDataBO();
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



                if (alertLevel !=0 && category != null) {
                    switch (category) {
                        case "curr":
                            curr = true;
                            processDataAlert = true;
                            break;
                        case "volt":
                            volt = true;
                            processDataAlert = true;
                            break;
                        case "time":
                            time = true;
                            processDataAlert = true;
                            break;
                        case "capa":
                            capa = true;
                            processDataAlert = true;
                            break;
                        case "temp":
                            temp = true;
                            processDataAlert = true;
                            break;
                    }

                    /*String sql = "insert into tx_alert_info(`handle`,`site`,`remark`,`sfc`,`category`,`alert_sequence_number`,`tx_alert_list_info_bo`,`status`,`process_info_bo`,`timestamp`,`erp_resource_bo`,`channel_id`,`alert_level`,`description`,`up_limit`,`low_limit`,`original_process_data_bo`,`created_data_time`,`created_user`,`modified_date_time`,`modified_user`) " +
                            "values ('"+handle +"','" + site + "','" + remark + "','" + sfc + "','" + category + "'," + alertSquenceNumber + ",'" + txAlertListInfoBo + "','" + status + "','" + processInfoBo + "','" + timestamp + "','" + erpResourceBo + "'," + channelId + "," + alertLevel + ",'" + description + "'," + upLimit + "," + lowLimit + ",'" + originalProBo + "','" + createDateTime + "','" + createUser + "','" + modifiedDateTime + "','" + modifiedUser + "')";
                    */
                    String sql ="insert into TX_ALERT_INFO(HANDLE,SITE,REMARK,SFC,CATEGORY,ALERT_SEQUENCE_NUMBER,TX_ALERT_LIST_INFO_BO,STATUS,PROCESS_INFO_BO,TIMESTAMP,ERP_RESOURCE_BO,CHANNEL_ID,ALERT_LEVEL,DESCRIPTION,UP_LIMIT,LOW_LIMIT,ORIGINAL_PROCESS_DATA_BO,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1,alertHandle);
                    pst.setString(2,site);
                    pst.setString(3,remark);
                    pst.setString(4,sfc);
                    pst.setString(5,category);
                    pst.setInt(6,alertSquenceNumber);
                    pst.setString(7,AlertListInfohandle);
                    pst.setString(8,status);
                    pst.setString(9,processInfoBo);
                    pst.setString(10,timestamp.toString());             //注意
                    pst.setString(11,erpResourceBo);
                    pst.setInt(12,channelId);
                    //pst.setInt(13,alertLevel);                         //更改类型
                    pst.setString(14,description);
                    pst.setBigDecimal(15,upLimit);
                    pst.setBigDecimal(16,lowLimit);
                    pst.setString(17,originalProBo);
                    pst.setString(18,timestamp.toString());             //注意
                    pst.setString(19,"HJHJ");
                    pst.setString(20,timestamp.toString());                //注意
                    pst.setString(21,"NMCX");
                    pst.addBatch();
                }
            }
            if (pst != null) {
                pst.executeBatch();
                conn.commit();
                pst.clearBatch();        //待定
            }

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

                    subHandle = "TxOriginalSubChannelDataBO:"+procehandle+testingSubChannel.getSubChannelName();//:< TX_ORIGINAL_PROCESS_DATA_BO>,<SUB_CHANNEL_ID>
                    subChannelId = Integer.valueOf(testingSubChannel.getSubChannelName().substring(testingSubChannel.getSubChannelName().length()-1));
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
                    //第二张表
                    //String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA values('"+subHandle+"','"+procehandle+"',"+subChannelId+",'"+site+"','"+remark+"','"+sfc+"','"+resourceId+"',"+channelId+","+subSequenceId+","+subCycle+","+subStepId+","+subTestTimeDuration+","+subVolt+","+subCurr+","+subIr+","+subTemp+","+subChargeCapacity+","+subDisChargeCapacity+","+subChargeEnergy+","+subDisChargeEnergy+",'"+subTimestamp+"',"+subDataFlag+","+subWorkType+"','"+" "+"','"+" "+"','"+" "+"','"+" "+"','"+")";
                    String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA(HANDLE,TX_ORIGINAL_PROCESS_DATA_BO,SUB_CHANNEL_ID,SITE,REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,CYCLE,STEP_ID,TEST_TIME_DURATION,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,TIMESTAMP,DATA_FLAG,WORK_TYPE,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pst = conn.prepareStatement(subsql);//插入子通道表
                    pst = conn.prepareStatement(subsql);//插入子通道表
                    pst.setString(1,subHandle);
                    pst.setString(2,procehandle);
                    pst.setInt(3,subChannelId);
                    pst.setString(4,site);
                    pst.setString(5,remark);
                    pst.setString(6,sfc);
                    pst.setString(7,resourceId);
                    pst.setInt(8,channelId);
                    pst.setInt(9,subSequenceId);
                    pst.setInt(10,subCycle);
                    pst.setInt(11,subStepId);
                    pst.setBigDecimal(12,subTestTimeDuration);
                    pst.setBigDecimal(13,subVolt);
                    pst.setBigDecimal(14,subCurr);
                    pst.setBigDecimal(15,subIr);
                    pst.setBigDecimal(16,subTemp);
                    pst.setBigDecimal(17,subChargeCapacity);
                    pst.setBigDecimal(18,subDisChargeCapacity);
                    pst.setBigDecimal(19,subChargeEnergy);
                    pst.setBigDecimal(20,subDisChargeEnergy);
                    pst.setString(21,subTimestamp.toString());
                    pst.setInt(22,subDataFlag);
                    pst.setInt(23,subWorkType);
                    pst.setString(24,subTimestamp.toString());
                    pst.setString(25,"qqq");
                    pst.setString(26,subTimestamp.toString());
                    pst.setString(27,"www");

                    pst.addBatch();//待定
                }
                if (pst != null) {
                    pst.executeBatch();
                    conn.commit();
                }
            }
            //第三张表
            /*String procesql = "insert into TX_ORIGINAL_PROCESS_DATA(`handle`,`site`,`remark`,`sfc`,`resource_id`,`channel_id`,`sequence_id`,`cycle`,`step_id`,`step_name`,`test_time_duration`,`timestamp`,`sv_ic_range`,`sv_iv_range`,`pv_voltage`,`pv_current`,`pv_ir`,`pv_temperature`,`pv_charge_capacity`,`pv_discharge_capacity`,`pv_charge_energy`,`pv_discharge_energy`,`pv_sub_channel_1`,`pv_sub_channel_2`,`pv_sub_channel_3`,`pv_sub_channel_4`,`pv_sub_channel_5`,`pv_sub_channel_6`,`pv_data_flag`,`pv_work_type`,`tx_is_exceptional`,`tx_alert_current`,`tx_alert_voltage`,`tx_alert_temperature`,`tx_alert_capacity`,`tx_alert_duration`,`tx_alert_category1`,`tx_alert_category2`,`tx_root_remark`,`st_business_cycle`,`created_data_time`,`created_user`,`modified_date_time`,`modified_user`) " +
                    "values ('" + procehandle + "','" + site + "','" + remark + "','" + sfc + "','" + resourceId + "'," + channelId + "," + sequenceId + "," + cycle + "," + stepId + ",'" + stepName + "'," + testTimeDuration + ",'" + testingmesstimestamp + "'," + svIcRange + "," + svIvRange + "," + pvVoltage + "," + pvCurrent + "," + pvIr + "," + pvTemperature + "," + pvChargeCapacity + "," + pvDischargeCapacity + "," + pvChargeEnergy + "," + pvDischargeEnergy + ",'" + subchannel1 + "','" + subchannel2 + "','" + subchannel3 + "','" + subchannel4 + "','" + subchannel5 + "','" + subchannel6 + "'," + pvDataFlag + "," + pvWorkType + ",'" + processDataAlert + "','" + curr + "','" + volt + "','" + temp + "','" + capa + "','" + time + "','" + "预留字段" + "','" + "预留字段" + "','" + "rootremark" + "'," + businessCycle + ",'" + createDateTime + "','" + createUser + "','" + modifiedDateTime + "','" + modifiedUser + "')";
*/
            String procesql = "insert into TX_ORIGINAL_PROCESS_DATA VALUES('" + procehandle + "','" + site + "','" + remark + "','" + sfc + "','" + resourceId + "'," + channelId + "," + sequenceId + "," + cycle + "," + stepId + ",'" + stepName + "'," + testTimeDuration + ",'" + testingmesstimestamp + "'," + svIcRange + "," + svIvRange + "," + pvVoltage + "," + pvCurrent + "," + pvIr + "," + pvTemperature + "," + pvChargeCapacity + "," + pvDischargeCapacity + "," + pvChargeEnergy + "," + pvDischargeEnergy + ",'" + subchannel1 + "','" + subchannel2 + "','" + subchannel3 + "','" + subchannel4 + "','" + subchannel5 + "','" + subchannel6 + "'," + pvDataFlag + "," + pvWorkType + ",'" + processDataAlert + "','" + curr + "','" + volt + "','" + temp + "','" + capa + "','" + time + "','" +"category1"+ "','" +"category2" + "','"+ "rootremark" + "'," + businessCycle + ",'" + createDateTime + "','" + createUser + "','" + modifiedDateTime + "','" + modifiedUser + "',"+stepLogicNumber+")";

            pst = conn.prepareStatement(procesql);//插入流程结果表
            pst.execute();//待定
            //插入第四张表

            String alertListsql = "insert into TX_ALERT_LIST_INFO values('"+AlertListInfohandle+"','"+site+"','"+alertListId+"','"+status+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+""+"','"+createDateTime + "','" + createUser + "','" + modifiedDateTime + "','" + modifiedUser +"')";
            pst = conn.prepareStatement(alertListsql);//插入报警列表

            pst.execute();//待定



        } catch (SQLException e) {
            conn.rollback();            //回滚
            e.printStackTrace();
            System.out.println("插入告警数据错误");
        } finally {
            db.close(conn, pst);
        }

        return false;
    }

    public static void main(String[] args) {
        IResultData iResultData = new ResultDataImpl();

    }
}
