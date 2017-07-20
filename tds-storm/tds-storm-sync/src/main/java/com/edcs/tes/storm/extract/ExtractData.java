package com.edcs.tes.storm.extract;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import com.edcs.tds.common.util.DBHelperUtils;
import com.edcs.tes.storm.sync.DataSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cong.xiang on 2017/7/11.
 */
//remark NVARCHAR(80) 流程号  cycleId INTEGER 业务循环号   stepId INTEGER 工步号
public class ExtractData {
    private DBHelperUtils dbHelperUtils;
    private final Logger logger = LoggerFactory.getLogger(DataSyncService.class);
    public ExtractData(DBHelperUtils dbHelperUtils) {
        this.dbHelperUtils = dbHelperUtils;
    }
    public String extractData(String remark, int cycleId, int stepId) {
        Connection conn = dbHelperUtils.getConnection();
        CallableStatement c = null;
        String state = null;
        try {
            c = conn.prepareCall("{call \"TES\".\"EXTRACTIONDATA\"(?, ?, ?, ?)}");
            c.setString(1, remark);
            c.setInt(2, cycleId);
            c.setInt(3, stepId);
            c.registerOutParameter(4, Types.VARCHAR);
            c.execute();
            state = c.getString(4);
            System.out.println("c.getString(4)"+c.getString(4));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
        }
        return state;
    }


}
