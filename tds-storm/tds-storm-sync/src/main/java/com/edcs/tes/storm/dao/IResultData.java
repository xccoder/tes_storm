package com.edcs.tes.storm.dao;

import com.edcs.tds.common.model.TestingResultData;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by CaiSL2 on 2017/7/4.
 */
public interface IResultData {
    boolean insertResultData(List<TestingResultData> testingResultDatas) throws SQLException;
}
