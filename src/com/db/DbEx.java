package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbEx {
	
	private final String driver = "com.mysql.cj.jdbc.Driver";
	private final String url = "jdbc:mysql://localhost:3306/member?autoReconnect=true";
	private final String id = "root";
	private final String pw = "1234";
	Connection conn= null;
	PreparedStatement ps= null;
	
	public void initTable() {
		try {
	         Class.forName("com.mysql.cj.jdbc.Driver");
	         conn = DriverManager.getConnection(url, id, pw);
	         String sql1 = "DROP TABLE IF EXISTS memtest";
	         ps = conn.prepareStatement(sql1);
	         ps.executeUpdate();
	         String sql2 = "create table memTest(num int auto_increment primary key, id varchar(10) not null unique, name varchar(20) not null, phone varchar(20),  addr varchar(20), pass varchar(20) not null)";
	         ps = conn.prepareStatement(sql2);
	         ps.executeUpdate();
		}catch (SQLException |ClassNotFoundException e) {
			e.printStackTrace();
		}finally {
			if(conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if(ps != null)
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public Connection getConnection() throws SQLException {
	     try {
	         Class.forName("com.mysql.cj.jdbc.Driver");
	     } catch (ClassNotFoundException e) {
	         throw new SQLException("JDBC Driver not found.", e);
	     }
	     return DriverManager.getConnection(url, id, pw);
	}	
//	public DbEx(){
//		try {
//			Class.forName(driver);
//			conn = DriverManager.getConnection(url, id, pw);
//		}catch(ClassNotFoundException |SQLException e) {
//			e.printStackTrace();
//		}
//
//	}
//	
//	public Connection getConnection() {
//		return conn;
}