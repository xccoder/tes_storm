package com.edcs.tds.storm.service;

import com.edcs.tds.common.util.DBHelperUtils;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by cong.xiang on 2017/7/11.
 */
//remark NVARCHAR(80) 流程号  cycleId INTEGER 业务循环号   stepId INTEGER 工步号
public class ExtractData {
    public String extractData(String remark, int cycleId, int stepId) {
        ComboPooledDataSource comboPooledDataSource = null;
        DBHelperUtils dbHelperUtils = new DBHelperUtils();
        dbHelperUtils.setDataSource(comboPooledDataSource);
        Connection conn = dbHelperUtils.getConnection();
        CallableStatement c = null;
        try {
            c = conn.prepareCall("{call \"TES\".\"EXTRACTIONDATA\"(?, ?, ?, ?)}");
            c.setString(1, remark);
            c.setInt(2, cycleId);
            c.setInt(3, stepId);
            c.registerOutParameter(4, Types.VARCHAR);
            c.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            return c.getString(4);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
