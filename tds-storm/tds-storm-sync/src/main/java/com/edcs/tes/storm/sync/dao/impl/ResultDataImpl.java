package com.edcs.tes.storm.sync.dao.impl;

import com.edcs.tds.common.model.TestingResultData;
import com.edcs.tds.common.model.TestingSubChannel;
import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tds.common.util.JsonUtils;
import com.edcs.tes.storm.sync.dao.IResultData;

import java.math.BigDecimal;
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
    private String site = null ;
    private String sfc = null ;
    private int channelId ;

    private Date createDateTime ;
    private String createUser  = null;
    private Date modifiedDateTime ;
    private String modifiedUser  = null;

    private int sequenceId ;

    private int businessCycle ;
    private int cycle ;
    private String procehandle  = null;
    private BigDecimal pvChargeCapacity ;
    private BigDecimal pvChargeEnergy;
    private BigDecimal pvCurrent ;
    private int pvDataFlag ;
    private BigDecimal pvDischargeCapacity  ;
    private BigDecimal pvDischargeEnergy ;
    private BigDecimal pvIr;
    private BigDecimal pvTemperature ;
    private BigDecimal pvVoltage ;
    private String pvWorkType = null;
    private String resourceId = null;
    private int stepId ;
    private String stepName  = null;
    private List<TestingSubChannel> subChannels  = null;
    private String subchannel1 = null;
    private String subchannel2 = null;
    private String subchannel3 = null;
    private String subchannel4 = null;
    private String subchannel5 = null;
    private String subchannel6 = null;

    private BigDecimal svIcRange ;
    private BigDecimal svIvRange ;
    private BigDecimal testTimeDuration ;
    private Date testingmesstimestamp ;
    @Override
    public boolean insertResultData(List<TestingResultData> testingResultDatas) {
         String category ;//场景
         int alertSquenceNumber;
         String txAlertListInfoBo ;
         String status ;
         String processInfoBo ;
         Date timestamp  ;
         String erpResourceBo ;
         String alertLevel ;
         String description  ;
         BigDecimal upLimit ;
         BigDecimal lowLimit ;
         String originalProBo ;
         String handle ;

        for (TestingResultData testingResultData : testingResultDatas) {

             handle = testingResultData.getHandle();
             remark = testingResultData.getTestingMessage().getRemark();
             site = testingResultData.getSite();
             sfc = testingResultData.getTestingMessage().getSfc();
             category = testingResultData.getCategory();//场景
             alertSquenceNumber = testingResultData.getAltetSequenceNumber();
             txAlertListInfoBo = testingResultData.getTxAlertListInfoBO();
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
             procehandle = testingResultData.getTestingMessage().getMessageId();
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
             resourceId = testingResultData.getTestingMessage().getResourceId();
             stepId = testingResultData.getTestingMessage().getStepId();
             stepName = testingResultData.getTestingMessage().getStepName();
             subChannels = testingResultData.getTestingMessage().getSubChannel();


             svIcRange = testingResultData.getTestingMessage().getSvIcRange();
             svIvRange = testingResultData.getTestingMessage().getSvIvRange();
             testTimeDuration = testingResultData.getTestingMessage().getTestTimeDuration();
             testingmesstimestamp = testingResultData.getTestingMessage().getTimestamp();


            if (!alertLevel.equals("null") && category !=null) {
                switch (category){
                    case "curr" : curr = true;processDataAlert = true;
                    case "volt" : volt = true;processDataAlert = true;
                    case "time" : time = true;processDataAlert = true;
                    case "capa" : capa = true;processDataAlert = true;
                    case "temp" : temp = true;processDataAlert = true;
                }

               /* if (category.equals("curr")) {
                    curr = true;
                    processDataAlert = true;
                } else if (category.equals("volt")) {
                    volt = true;
                    processDataAlert = true;
                } else if (category.equals("time")) {
                    time = true;
                    processDataAlert = true;
                } else if (category.equals("capa")) {
                    capa = true;
                    processDataAlert = true;
                } else if (category.equals("temp")) {
                    temp = true;
                    processDataAlert = true;
                }*/

                String sql = "insert into tx_alert_info(`handle`,`site`,`remark`,`sfc`,`category`,`alert_sequence_number`,`tx_alert_list_info_bo`,`status`,`process_info_bo`,`timestamp`,`erp_resource_bo`,`channel_id`,`alert_level`,`description`,`up_limit`,`low_limit`,`original_process_data_bo`,`created_data_time`,`created_user`,`modified_date_time`,`modified_user`) " +
                        "values (" + handle + "," + site + "," + remark + "," + sfc + "," + category + "," + alertSquenceNumber + "," + txAlertListInfoBo + "," + status + "," + processInfoBo + "," + timestamp + "," + erpResourceBo + "," + channelId + "," + alertLevel + "," + description + "," + upLimit + "," + lowLimit + "," + originalProBo + "," + createDateTime + "," + createUser + "," + modifiedDateTime + "," + modifiedUser + ")";
                try {
                    db.insert(sql);
                } catch (Exception e) {
                    //
                }

            }
        }
        if (subChannels!=null) {
            for (TestingSubChannel testingSubChannel : subChannels) {
                switch (testingSubChannel.getSubChannelName()){
                    case "pvSubChannelData1" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                    case "pvSubChannelData2" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                    case "pvSubChannelData3" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                    case "pvSubChannelData4" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                    case "pvSubChannelData5" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                    case "pvSubChannelData6" : subchannel1 = JsonUtils.toJson(testingSubChannel);
                }
            }
        }
        String procesql = "insert into TX_ORIGINAL_PROCESS_DATA(`handle`,`site`,`remark`,`sfc`,`resource_id`,`channel_id`,`sequence_id`,`cycle`,`step_id`,`step_name`,`test_time_duration`,`timestamp`,`sv_ic_range`,`sv_iv_range`,`pv_voltage`,`pv_current`,`pv_ir`,`pv_temperature`,`pv_charge_capacity`,`pv_discharge_capacity`,`pv_charge_energy`,`pv_discharge_energy`,`pv_sub_channel_1`,`pv_sub_channel_2`,`pv_sub_channel_3`,`pv_sub_channel_4`,`pv_sub_channel_5`,`pv_sub_channel_6`,`pv_data_flag`,`pv_work_type`,`tx_is_exceptional`,`tx_alert_current`,`tx_alert_voltage`,`tx_alert_temperature`,`tx_alert_capacity`,`tx_alert_duration`,`tx_alert_category1`,`tx_alert_category2`,`tx_root_remark`,`st_business_cycle`,`created_data_time`,`created_user`,`modified_date_time`,`modified_user`) " +
                                                        "values ("+ procehandle + "," + site + "," + remark + "," + sfc +"," + resourceId +"," +channelId+"," +sequenceId+"," +cycle+"," +stepId+"," +stepName+"," +testTimeDuration+"," +testingmesstimestamp+"," +svIcRange+"," +svIvRange+"," +pvVoltage+"," +pvCurrent+"," +pvIr+"," +pvTemperature+"," +pvChargeCapacity+"," +pvDischargeCapacity+"," +pvChargeEnergy+"," +pvDischargeEnergy+"," +subchannel1+"," +subchannel2+"," +subchannel3+"," +subchannel4+"," +subchannel5+"," +subchannel6+"," +pvDataFlag+"," +pvWorkType+"," +processDataAlert+"," +curr+"," +volt+"," +temp+"," +capa+"," +time+"," +"预留字段"+"," +"预留字段"+"," +"rootremark"+"," +businessCycle+"," +createDateTime+"," +createUser+"," +modifiedDateTime+"," +modifiedUser+")";
        try {
            db.insert(procesql);
        } catch (Exception e) {
            //
        }
        
        return false;
    }
}
