package com.edcs.tds.common.util;






import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;


public class DBHelperUtils {
	
	public static final String url = "jdbc:sap://172.26.164.10:30015/TDS";
	public static final String name = "com.sap.db.jdbc.Driver";
	public static final String user = "TDS";
	public static final String password = "Aa123456";
	public Connection conn = null;
    public ComboPooledDataSource dataSource;
	public DBHelperUtils() {  
		try {
			dataSource = new ComboPooledDataSource();
			dataSource.setJdbcUrl(url);
			dataSource.setDriverClass(name);
			dataSource.setUser(user);
			dataSource.setPassword(password);
			dataSource.setInitialPoolSize(3);
			dataSource.setMaxPoolSize(5);
			dataSource.setMaxIdleTime(1000);
            conn = dataSource.getConnection();
//		   Class.forName(name);//指定连接类型  
//		   conn = DriverManager.getConnection(url, user, password);//获取连接  
		} catch (Exception e) {  
	           e.printStackTrace();  
		}  
	}
	
	public ResultSet selectList(String sql){
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean insert(String sql){
		PreparedStatement pst = null;
		boolean boo = false;
		try {
			conn = dataSource.getConnection();
			pst = conn.prepareStatement(sql);
			boo = pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return boo;
	}

	public void close() {
		try {
			this.conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
