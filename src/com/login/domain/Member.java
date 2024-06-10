package com.login.domain;

public class Member {
	
	private int num;
	private String id;
	private String name;
	private String phone;
	private String addr;
	private String pass;
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public 	Member() {}
	
	public Member(int num, String id, String name, String phone, String addr, String pass) {
		super();
		this.num = num;
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.addr = addr;
		this.pass = pass;
	}
	@Override
	public String toString() {
		return "Member [num=" + num + ", id=" + id + ", name=" + name + ", phone=" + phone + ", addr=" + addr
				+ ", pass=" + pass + "]";
	}
	
	
}