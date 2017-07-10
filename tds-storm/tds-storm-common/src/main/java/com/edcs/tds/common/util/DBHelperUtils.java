package com.edcs.tds.common.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DBHelperUtils {
    public ComboPooledDataSource dataSource;
    
    public ComboPooledDataSource getDataSource() {
		return dataSource;
	}
    public void setDataSource(ComboPooledDataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Connection getConnection(){
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/*public ResultSet selectList(String sql){
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			Connection conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}*/
	
	public boolean insert(String sql){
		PreparedStatement pst = null;
		boolean boo = false;
		try {
			Connection conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			boo = pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boo;
	}
	public void close(Connection conn,PreparedStatement pst,ResultSet result){
		try {
			if(result!=null) result.close();
			if(pst!=null) pst.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void close(Connection conn,PreparedStatement pst){
		try {
			if(pst!=null) pst.close();
			if(conn!=null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
