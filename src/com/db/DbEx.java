package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbEx {
	
	private static final String driver = "com.mysql.cj.jdbc.Driver";
	private static final String url = "jdbc:mysql://localhost:3306/member?autoReconnect=true";
	private static final String id = "root";
	private static final String pw = "1234";
	private static Connection conn= null;
	
//	public void initTable() {
//		try {
//	         Class.forName("com.mysql.cj.jdbc.Driver");
//	         conn = DriverManager.getConnection(url, id, pw);
//	         String sql1 = "DROP TABLE IF EXISTS memtest";
//	         ps = conn.prepareStatement(sql1);
//	         ps.executeUpdate();
//	         String sql2 = "create table memTest(num int auto_increment primary key, id varchar(10) not null unique, name varchar(20) not null, phone varchar(20),  addr varchar(20), pass varchar(20) not null)";
//	         ps = conn.prepareStatement(sql2);
//	         ps.executeUpdate();
//		}catch (SQLException |ClassNotFoundException e) {
//			e.printStackTrace();
//		}finally {
//			if(conn != null)
//				try {
//					conn.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			if(ps != null)
//				try {
//					ps.close();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
//	}
	
	public static void initializeConnection() {
		try {
			if(conn == null || conn.isClosed()) {
				conn = DriverManager.getConnection(url, id, pw);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static Connection getConn() {
		return conn;
	}
	
	public static void closeConn() {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
				System.out.println("db 연결 종료");
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
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