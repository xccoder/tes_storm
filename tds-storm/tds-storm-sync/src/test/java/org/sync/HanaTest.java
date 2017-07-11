package org.sync;

import com.edcs.tds.common.util.DBHelperUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by CaiSL2 on 2017/7/6.
 */
public class HanaTest {
    public static void main(String[] args) {
        DBHelperUtils db = new DBHelperUtils();
        Connection conn = null;
        PreparedStatement pst = null;
        String subHandle = "QWQW";
        int subChannelId = 1;//辅助通道id，从辅助通道name上截取
        int subSequenceId = 3;
        int subCycle = 3;
        int subStepId = 5;
        BigDecimal subTestTimeDuration = new BigDecimal(20);
        BigDecimal subVolt = new BigDecimal(20);
        BigDecimal subCurr = new BigDecimal(20);
        BigDecimal subIr = new BigDecimal(20);
        BigDecimal subTemp = new BigDecimal(20);
        BigDecimal subChargeCapacity = new BigDecimal(20);
        BigDecimal subDisChargeCapacity = new BigDecimal(20);
        BigDecimal subChargeEnergy = new BigDecimal(20);
        BigDecimal subDisChargeEnergy = new BigDecimal(20);
        Date date = new Date();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String subTimestamp = format.format(date);
        System.out.println(subTimestamp);
        int subDataFlag = 7;
        int subWorkType = 0;
        String procehandle = "AAAAAAAAAA";
        String remark = "AA";
        String sfc = "AAA";
        String resourceId = "SS";
        int channelId = 5;
        String site = "A";
        for (int i = 0; i < 3; i++) {
            try {
                //String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA values('" + subHandle + "','" + procehandle + "'," + subChannelId + ",'" + site + "','" + remark + "','" + sfc + "','" + resourceId + "'," + channelId + "," + subSequenceId + "," + subCycle + "," + subStepId + "," + subTestTimeDuration + "," + subVolt + "," + subCurr + "," + subIr + "," + subTemp + "," + subChargeCapacity + "," + subDisChargeCapacity + "," + subChargeEnergy + "," + subDisChargeEnergy + ",'" + subTimestamp + "'," + subDataFlag + "," + subWorkType + ",'" + subTimestamp + "','tt'," + subTimestamp + ",'er')";
                //String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA values('dd','ff',34,'WE','WE','WE','WE',2,3,4,5,6,7,8,9,1,2,3,4,5,'2017-07-06 16:24:30',5,6,'2017-07-06 16:24:30','TG','2017-07-06 16:24:30','JU')";
                String subsql = "insert into TX_ORIGINAL_SUB_CHANNEL_DATA(HANDLE,TX_ORIGINAL_PROCESS_DATA_BO,SUB_CHANNEL_ID,SITE,REMARK,SFC,RESOURCE_ID,CHANNEL_ID,SEQUENCE_ID,CYCLE,STEP_ID,TEST_TIME_DURATION,PV_VOLTAGE,PV_CURRENT,PV_IR,PV_TEMPERATURE,PV_CHARGE_CAPACITY,PV_DISCHARGE_CAPACITY,PV_CHARGE_ENERGY,PV_DISCHARGE_ENERGY,TIMESTAMP,DATA_FLAG,WORK_TYPE,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

                conn = db.getConnection();
                conn.setAutoCommit(false);
                pst = conn.prepareStatement(subsql);//插入子通道表
                pst.setString(1, subHandle + i);
                pst.setString(2, procehandle);
                pst.setInt(3, subChannelId);
                pst.setString(4, site);
                pst.setString(5, remark);
                pst.setString(6, sfc);
                pst.setString(7, resourceId);
                pst.setInt(8, channelId);
                pst.setInt(9, subSequenceId);
                pst.setInt(10, subCycle);
                pst.setInt(11, subStepId);
                pst.setBigDecimal(12, subTestTimeDuration);
                pst.setBigDecimal(13, subVolt);
                pst.setBigDecimal(14, subCurr);
                pst.setBigDecimal(15, subIr);
                pst.setBigDecimal(16, subTemp);
                pst.setBigDecimal(17, subChargeCapacity);
                pst.setBigDecimal(18, subDisChargeCapacity);
                pst.setBigDecimal(19, subChargeEnergy);
                pst.setBigDecimal(20, subDisChargeEnergy);
                pst.setString(21, subTimestamp);
                pst.setInt(22, subDataFlag);
                pst.setInt(23, subWorkType);
                pst.setString(24, subTimestamp);
                pst.setString(25, "qqq");
                pst.setString(26, subTimestamp);
                pst.setString(27, "www");

                System.out.println(pst);

                pst.addBatch();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("出错 了 吗");
            }
            System.out.println("结束");
            try {
                pst.executeBatch();
                conn.commit();

                pst.clearBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }
    }
}
