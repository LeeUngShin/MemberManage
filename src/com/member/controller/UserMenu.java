package com.member.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PipedWriter;
import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.Scanner;

import com.db.DbEx;
import com.member.domain.Member;
import com.member.exception.MyException;
import com.mysql.cj.exceptions.RSAException;
import com.mysql.cj.jdbc.BlobFromLocator;

public class UserMenu implements UserLogin {

	String id;
	String pw;
	Scanner scanner = new Scanner(System.in);
	int cnt = 0; // 로그인 실패 횟수
	boolean login = false; // 로그인 성공 여부
	List<Member> members = new ArrayList<>(); // 회원 db 저장
	Member member;// 로그인한 회원의 정보 저장
	String[] arr = new String[20]; // 파일 읽어와서 그 파일의 텍스트를 split()으로 쪼개서 배열에 저장

	private Connection conn = DbEx.getConn();

	public UserMenu() {
	}

	/**
	 * 로그인 화면+기능
	 * 
	 * @return
	 */
	public boolean login() {
		cnt = 0;
		// System.out.println(members);
		System.out.println("************************************************");
		System.out.println("\t\t로그인");
		System.out.println("************************************************");
		while (cnt < 3) {
			System.out.println("아이디를 입력하세요: ");
			id = scanner.nextLine();
			System.out.println("비밀번호를 입력하세요.");
			pw = scanner.nextLine();
			String sql = "select * from memtest where id= ? and pass=?";
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
				ps.setString(1, id);
				ps.setString(2, pw);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int num = rs.getInt("Mnum");
						String id = rs.getString("id");
						String name = rs.getString("name");
						String phone = rs.getString("phone");
						String addr = rs.getString("addr");
						String pw = rs.getString("pass");
						member = new Member(num, id, name, phone, addr, pw);
						System.out.println("로그인 성공");
						System.out.println("현재 회원 정보 : " + member);
						return true;
					} else {
						System.out.println("로그인 실패");
						cnt++;
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("로그인 횟수 초과");
		return false;

	}

	/**
	 * 메뉴 출력
	 */
	public void menu() {
		System.out.println("************************************************");
		System.out.println("\t\t" + id + "님 안녕하세요?");
		System.out.println("************************************************");
		System.out.println(" 1. 회원 정보 확인하기\t 2. 회원 정보 수정하기");
		System.out.println(" 3. 회원 탈퇴\t\t 4. 주문");
		System.out.println(" 5. 종료");
		System.out.println("메뉴 번호를 선택해 주세요.");

	}

	/**
	 * 회원 정보 조회
	 */
	@Override
	public void readMember() {
		System.out.println("조회시 나와야한 정보 : " + member);
		String sql = "select * from memtest where id = ?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, member.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					System.out.println("내 정보");
					System.out.println("아이디 : " + rs.getString("id"));
					System.out.println("이름 : " + rs.getString("name"));
					System.out.println("전화번호 : " + rs.getString("phone"));
					System.out.println("주소 : " + rs.getString("addr"));
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @throws MyException
	 */
	@Override
	public boolean updateMember() {

		String sql = "UPDATE memtest SET name=?, phone=?, addr=? WHERE id=?";

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			System.out.print("수정할 이름 : ");
			String name = scanner.nextLine();
			System.out.print("수정할 전화번호 : ");
			String phone = scanner.nextLine();
			System.out.print("수정할 주소 : ");
			String addr = scanner.nextLine();
			ps.setString(1, name);
			ps.setString(2, phone);
			ps.setString(3, addr);
			ps.setString(4, member.getId());
			int result = ps.executeUpdate();
			System.out.println("update 반환값 : " + result);
			member.setName(name);
			member.setPhone(phone);
			member.setAddr(addr);
			System.out.println("수정후 정보 : " + member);
			Iterator<Member> iter = members.iterator();
			while (iter.hasNext()) {
				Member m = iter.next();
				if (member.getId().equals(m.getId()))
					m.setName(name);
				m.setPhone(phone);
				m.setAddr(addr);
			}
			System.out.println("수정 후 전체 목록 : " + members);
			writeFileMember();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 회원 탈퇴
	 */
	@Override
	public boolean deleteMember() {
		System.out.println("비빌번호를 입력하세요 : ");
		String pw = scanner.nextLine();
		if (!(pw.equals(member.getPw()))) {
			System.out.println("비밀번호가 틀렸습니다.");
			return false;
		}
		String sql = "delete from memtest where id=?";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, member.getName());
			int r = ps.executeUpdate();
			System.out.println("delete 반환값 : " + r);
			writeFileMember();
			Iterator<Member> iter = members.iterator();
			while (iter.hasNext()) {
				Member m = iter.next();
				if (member.getId().equals(m.getId()))
					iter.remove();
			}
			System.out.println("탈퇴 후 전체 목록 : " + members);
			writeFileMember();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	@Override
	public boolean itemOrder() {

		String sql = "select * from items";
		String sql2 = "select INum, price,stock from items where INum=?";
		String sql3 = "insert into orders(stock, totalprice, FMnum,FINum) values(?,?,?,?)";
		String sql4 = "update items set stock=? where INum=?";

		int totalprice = 0;
		int nowStock = 0;
		int updateStock = 0;

		try (PreparedStatement ps = conn.prepareStatement(sql);
				PreparedStatement ps2 = conn.prepareStatement(sql2);
				PreparedStatement ps3 = conn.prepareStatement(sql3);
				PreparedStatement ps4 = conn.prepareStatement(sql4)) {
			System.out.printf("%-7s%-7s%-7s%-7s\n", "번호", "이름", "수량", "가격");
			try (ResultSet rs = ps.executeQuery()) {
				int num = 0, stock = 0, price = 0;
				String name = "";
				while (rs.next()) {
					num = rs.getInt("INum");
					name = rs.getString("name");
					stock = rs.getInt("stock");
					price = rs.getInt("price");
					System.out.printf("%-9d%-7s%-9d%-7d\n", num, name, stock, price);
				}
			}
			System.out.println("구매할 상품번호를 입력하세요");
			int itemNum = scanner.nextInt();
			System.out.println("구매할 상품의 수량을 입력하세요");
			int amount = scanner.nextInt();
			ps2.setInt(1, itemNum);
			int number = 0;
			try (ResultSet rs2 = ps2.executeQuery()) {
				while(rs2.next()) {
					int price = rs2.getInt("price");
					totalprice = price * amount;
					number = rs2.getInt("INum");
					nowStock = rs2.getInt("Stock");
					if (nowStock < amount) {
						System.out.println("주문 수량이 재고보다 많습니다.!");
						return false;
					}
					System.out.println("주문상품번호 : " + number + ", 주문상품 가격 : " + price + ", 주문상품 수량 : " + nowStock);
				}
				updateStock = nowStock - amount;
			}

			ps3.setInt(1, amount);
			ps3.setInt(2, totalprice);
			ps3.setInt(3, member.getNum());
			ps3.setInt(4, number);
			;
			int result = ps3.executeUpdate();
			System.out.println("상품구매 : " + result);

			ps4.setInt(1, updateStock);
			ps4.setInt(2, number);
			result = ps4.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("여기걸림");
		}

		return true;
	}
//	/**
//	 * 파일 읽어와 회원 정보 저장
//	 */
//	@Override
//	public void readFileMember() {
//		try {
//			File file = new File("members.txt");
//			if (!(file.exists()))
//				file.createNewFile();
//			FileReader fis = new FileReader(file);
//			BufferedReader br = new BufferedReader(fis);
//			String str; // 파일에서 읽어온 문자열 저장하기 위한 변수
//
//			while ((str = br.readLine()) != null) { // members2.txt 파일을 한 줄씩 읽어와서
//				arr = str.split(", "); // 공백을 기준으로 배열의 저장
//				// System.out.println("현재배열 : "+Arrays.toString(arr));
//				String[] arrSplit = new String[2];
//				String[] memberInput = new String[6];
//				for (int i = 0; i < 6; i++) {
//					arrSplit = arr[i].split(" : ");
//					memberInput[i] = arrSplit[1];
//					memberInput[i] = memberInput[i].trim();
//				}
//				Member member = new Member(); // 위 배열에서 데이터만 뽑아서 Member 객체에 저장
//				member.setNum(Integer.parseInt(memberInput[0]));
//				member.setId(memberInput[1]);
//				member.setName(memberInput[2]);
//				member.setPhone(memberInput[3]);
//				member.setAddr(memberInput[4]);
//				member.setPw(memberInput[5]);
//				System.out.println("파일 읽어서 : " + member);
//
////				member.setNum(Integer.parseInt(arr[2]));
////				member.setId(arr[5]);
////				member.setName(arr[8]);
////				member.setPhone(arr[11]);
////				member.setAddr(arr[14]);
////				member.setPass(arr[17]);
//				members.add(member); // 데이터를 저장한 Member 객체를 members 리스트에 저장
//				System.out.println("회원db : " + members);
//			}
//			fis.close();
//			System.out.println("파일 읽기 완");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * 회원 수정 및 탈퇴 시 반영 해 파일 수정
	 */
	@Override
	public void writeFileMember() {
		AdminMenu am = new AdminMenu();
		File file = new File("members.txt"); // members2.txt 파일 담은 File 객체
		try {
			if (!(file.exists()))
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
			System.out.println("txt에 저장할 목록 : " + members);
			for (Member m : members) {
				fw.write("회원번호 : " + m.getNum() + ", 아이디 : " + m.getId() + ", 이름 : " + m.getName() + ", 연락처 : "
						+ m.getPhone() + ", 주소 : " + m.getAddr() + ", 비밀번호 : " + m.getPw() + "\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}