package com.member.domain;

public class Member {
	
	private int num;  // 번호
	private String id;  // 아이디
	private String name;  // 이름
	private String phone;  // 전화번호
	private String addr;  // 주소
	private String pw;  // 비밀번호
	
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
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public Member() {
		super();
	}
	public Member(int num, String id, String name, String phone, String addr, String pw) {
		super();
		this.num = num;
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.addr = addr;
		this.pw = pw;
	}
	@Override
	public String toString() {
		return "Member [num=" + num + ", id=" + id + ", name=" + name + ", phone=" + phone + ", addr=" + addr + ", pw="
				+ pw + "]";
	}	
}