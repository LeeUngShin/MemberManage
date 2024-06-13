package com.member.domain;

public class Item {

	private int INum;  // 물건 번호
	private String name;  // 이름
	private int stock;  // 수량 
	private int FCNum;  // 카테고리 번호(외래키)
	public int getINum() {
		return INum;
	}
	public void setINum(int iNum) {
		INum = iNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	public int getFCNum() {
		return FCNum;
	}
	public void setFCNum(int fCNum) {
		FCNum = fCNum;
	}
	@Override
	public String toString() {
		return "Item [INum=" + INum + ", name=" + name + ", stock=" + stock + ", FCNum=" + FCNum + "]";
	}

	
}
