package com.edcs.tds.storm.util;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class DBHelperUtils {
	
	public static final String url = "jdbc:sap://172.26.164.10:30015/TDS";
	public static final String name = "com.sap.db.jdbc.Driver";
	public static final String user = "TDS";
	public static final String password = "Aa123456";
	public Connection conn = null;
	public PreparedStatement pst = null;

	public DBHelperUtils(String sql) {  
		try {
			 Class.forName(name);//指定连接类型  
			 conn = DriverManager.getConnection(url, user, password);//获取连接  
			 pst = conn.prepareStatement(sql);//准备执行语句  
		} catch (Exception e) {  
	           e.printStackTrace();  
		}  
	}

	public void close() {
		try {
			this.conn.close();
			this.pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
