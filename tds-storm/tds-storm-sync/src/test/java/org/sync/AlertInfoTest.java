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
public class AlertInfoTest {
    public static void main(String[] args) {
        DBHelperUtils db =new DBHelperUtils();
        Connection conn = null;
        PreparedStatement pst = null;

        String site = "TT";
        String remark = "ASDFGH";
        String sfc = "SFDG";

        String category = "CURR";//场景
        int alertSquenceNumber = 45;
        String txAlertListInfoBo = "BFNFG";
        String status = "MKO";
        String processInfoBo="BUHVU";
        Date date = new Date();
        DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp=format.format(date);
        String erpResourceBo = "ETRYU-CIOJ";
        int channelId = 3;

        int alertLevel = 2;
        String description = "adafslkfkjskdfsd";
        BigDecimal upLimit = new BigDecimal(23);
        BigDecimal lowLimit = new BigDecimal(89);
        String originalProBo = "RTYUIOO";
        String handle = "ZXCVB";//每一个场景报警数据的handle

        String sql ="insert into TX_ALERT_INFO(HANDLE,SITE,REMARK,SFC,CATEGORY,ALERT_SEQUENCE_NUMBER,TX_ALERT_LIST_INFO_BO,STATUS,PROCESS_INFO_BO,TIMESTAMP,ERP_RESOURCE_BO,CHANNEL_ID,ALERT_LEVEL,DESCRIPTION,UP_LIMIT,LOW_LIMIT,ORIGINAL_PROCESS_DATA_BO,CREATED_DATE_TIME,CREATED_USER,MODIFIED_DATE_TIME,MODIFIED_USER) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            conn = db.getConnection();
            conn.setAutoCommit(false);
            pst = conn.prepareStatement(sql);//插入子通道表
            pst.setString(1,handle);
            pst.setString(2,site);
            pst.setString(3,remark);
            pst.setString(4,sfc);
            pst.setString(5,category);
            pst.setInt(6,alertSquenceNumber);
            pst.setString(7,txAlertListInfoBo);
            pst.setString(8,status);
            pst.setString(9,processInfoBo);
            pst.setString(10,timestamp);
            pst.setString(11,erpResourceBo);
            pst.setInt(12,channelId);
            pst.setInt(13,alertLevel);
            pst.setString(14,description);
            pst.setBigDecimal(15,upLimit);
            pst.setBigDecimal(16,lowLimit);
            pst.setString(17,originalProBo);
            pst.setString(18,timestamp);
            pst.setString(19,"HJHJ");
            pst.setString(20,timestamp);
            pst.setString(21,"NMCX");

            pst.addBatch();


            pst.executeBatch();
            conn.commit();
            pst.clearBatch();



        } catch (SQLException e) {
            System.out.println("出错 了 吗");
            e.printStackTrace();
        }
        System.out.println("结束");


    }
}
