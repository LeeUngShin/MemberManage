package com.member.controller;

import java.awt.print.Printable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.xml.catalog.Catalog;

import com.db.DbEx;
import com.member.domain.Member;
import com.member.exception.MyException;
import com.mysql.cj.protocol.x.ReusableOutputStream;

public class AdminMenu implements ManageMember {

	Scanner scanner = new Scanner(System.in);
	static int num = 2;
	String idString;
	String nameString;
	String phoneString;
	String addrString;
	String pwString;
	int cnt = 0;
	//private List<Member> ms = new ArrayList<Member>();
	Connection conn = null;  //db 연결 객체
	
	public AdminMenu() {  // db 연결 객체 생성
		conn = DbEx.getConn();
	}

	/**
	 * 관리자 계정 생성
	 * 
	 * @return
	 */
	public List<Member> createAdmin(List<Member> members) {
		String sql = "INSERT INTO memtest(id, name, phone, addr, pass) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			// 데이터베이스 연결 생성

			// PreparedStatement에 값 설정
			ps.setString(1, "admin");
			ps.setString(2, "관리자");
			ps.setString(3, "01000000000");
			ps.setString(4, "관리자주소");
			ps.setString(5, "admin");

			// SQL 실행
			int result = ps.executeUpdate();
			System.out.println("삽입 성공 여부: " + result);
			members.add(new Member(1, "admin", "관리자", "01000000000", "관리자주소", "admin"));
		} catch (Exception e) {
			// 예외 처리
			e.printStackTrace();
		}
		return members;
	}
	
	public void adminMenu() {
		System.out.println("*******************************************");
		System.out.println("\t\t회원 관리 프로그램");
		System.out.println("*******************************************");
		System.out.println("1. 고객 정보 등록하기\t2. 고객 정보 조회하기");
		System.out.println("3. 고객 정보 수정하기\t4. 고객 정보 삭제하기");
		System.out.println("5. 고객 정보 목록보기\t6. 고객 정보 파일출력");
		System.out.println("7. 상품 등록\t8. 전체주문내역 출력");
		System.out.println("9. 등록 상품 보기\t10. 종료");
		System.out.println("*******************************************");
		System.out.print("메뉴 번호를 선택해주세요");
	}

	/**
	 * 로그인 화면 및 기능
	 * 
	 * @return
	 */
	public boolean adminLogin(List<Member> members) {
		int cnt=0;
		System.out.println("*******************************************");
		System.out.println("\t\t로그인");
		System.out.println("*******************************************");
		while(cnt<3) {
			System.out.print("아이디를 입력하세요:");
			String id = scanner.nextLine();
			
			System.out.print("비밀번호를 입력하세요:");
			String pw = scanner.nextLine();
			String sql = "select * from memtest where MNum=1";
			
			try(PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()){
				String adminIdString="";
				String adminPwString="";
				while(rs.next()) {
					adminIdString = rs.getString("id");
					adminPwString = rs.getString("pass");
				}
				if(id.equals(adminIdString) && pw.equals(adminPwString)) {
					System.out.println("로그인 실패");
					return true;
				}
				else {
					cnt++;
					continue;
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("로그인 횟수 초과");	
		return false;
	}

	/**
	 * 회원 등록
	 */
	@Override
	public boolean createMmeber(List<Member> members) throws MyException {
		String sql = "INSERT INTO memtest(id, name, phone, addr, pass) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			System.out.print("등록하실 회월의 아이디를 입력하세요: ");
			idString = scanner.nextLine();
			System.out.print("등록하실 회월의 이름을 입력하세요: ");
			nameString = scanner.nextLine();
			System.out.print("등혹하실 회원의 연락처를 입력하세요: ");
			phoneString = scanner.nextLine();
			System.out.print("등록하실 회원의 주소를 입력하세요: ");
			addrString = scanner.nextLine();
			System.out.print("등록하실 회원의 비밀번호를 입력하세요: ");
			pwString = scanner.nextLine();
			ps.setString(1, idString);
			ps.setString(2, nameString);
			ps.setString(3, phoneString);
			ps.setString(4, addrString);
			ps.setString(5, pwString);
			int result = ps.executeUpdate();
			System.out.println("회원등록 : " + result);
			// 동일한 id가 없으면 회원 정보 등록
			members.add(new Member(num, idString, nameString, phoneString, addrString, pwString));
			num++; // 다음 회원의 회원 번호 1 증가 시키기 위해
			//System.out.println(members);
			//System.out.println("회원등록 후 : " + members);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public boolean registerItem() {
		
		String sqlString = "INSERT INTO items(name, stock, price) values(?,?,?)";
		try(PreparedStatement ps = conn.prepareStatement(sqlString)){
			System.out.println("상품명 : ");
			String name = scanner.nextLine();
			System.out.println("수량 : ");
			int stock=scanner.nextInt();
			System.out.println("가격 : ");
			int price = scanner.nextInt();
		
			ps.setString(1, name);
			ps.setInt(2, stock);
			ps.setInt(3, price);
			int result = ps.executeUpdate();
			System.out.println("상품등록 : " + result);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 회원 정보 조회
	 */
	@Override
	public Member readMmeber(int num) {
		Member member = null;
		boolean find = false;
		String sql = "select * from memtest where Mnum = ?";

		try (PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setLong(1, num);
			// 쿼리 실행
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int num1 = rs.getInt("Mnum");
					String id = rs.getString("id");
					String name = rs.getString("name");
					String phone = rs.getString("phone");
					String addr = rs.getString("addr");
					String pw = rs.getString("pass");
//	                System.out.println("num : "+num+" ,id"+id);
					member = new Member(num1, id, name, phone, addr, pw);
					System.out.println(member.toString());
					find = true;
				}
				if (!find) {
					while (!find) {
						System.out.println("해당 회원번호는 없습니다.");
						System.out.println("회원번호를 다시 입력하세요");
						int n = scanner.nextInt();
						ps.setLong(1, n);
						try (ResultSet rs2 = ps.executeQuery();) {
							while (rs2.next()) {
								int num1 = rs2.getInt("Mnum");
								String id = rs2.getString("id");
								String name = rs2.getString("name");
								String phone = rs2.getString("phone");
								String addr = rs2.getString("addr");
								String pw = rs2.getString("pass");
								member = new Member(num1, id, name, phone, addr, pw);
								System.out.println(member.toString());
								find = true;
							}
						}
					}
				}
			}
		} catch (

		SQLException e) {
			e.printStackTrace();
			System.out.println("2번 예외발생");
		}
		return member;
	}

	/**
	 * 회원 정보 수정
	 */
	@Override
	public boolean updateMmeber(List<Member> members) {
		System.out.println("수정할 회원 아이디를 입력해주세요.");
		idString = scanner.nextLine();
		String sql1 = "update memtest set name = ?, phone=?, addr=? where id = ?";
		String sql2 = "select * from memtest where id=?";
		try (PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);) {
			ps2.setString(1, idString);
			try (ResultSet rs2 = ps2.executeQuery()) {
				if(rs2.next()) {
					int num1 = rs2.getInt("Mnum");
					String id = rs2.getString("id");
					String name = rs2.getString("name");
					String phone = rs2.getString("phone");
					String addr = rs2.getString("addr");
					String pw = rs2.getString("pass");
					Member member = new Member(num1, id, name, phone, addr, pw);
					System.out.println("회원 현재 정보 : " + member.toString());
					System.out.print("수정할 이름 : ");
					nameString = scanner.nextLine();
					System.out.print("수정할 전화번호 : ");
					phoneString = scanner.nextLine();
					System.out.print("수정할 주소 : ");
					addrString = scanner.nextLine();
					System.out.println("비밀번호 입력 : ");
					pwString = scanner.nextLine();
					if (!pw.equals(pwString)) {
						System.out.println("비밀번호가 맞지 않습니다.");
						return false;
					} else {
						ps1.setString(1, nameString);
						ps1.setString(2, phoneString);
						ps1.setString(3, addrString);
						ps1.setString(4, idString);
						int result = ps1.executeUpdate();

					}
				}
				else {
					System.out.println("해당하는 아이디가 없습니다.");
					return false;
			}
		}
		for (Member m : members) {
			if (m.getId().equals(idString)) {
				m.setName(nameString);
				m.setPhone(phoneString);
				m.setAddr(addrString);
			}
		}
	}catch(

	SQLException e)
	{
				e.printStackTrace();
			}return true;

	}

	/**
	 * 회원 정보 삭제
	 */
	@Override
	public boolean deleteMmeber(List<Member> members) {
		System.out.println("삭제할 회원의 아이디를 입력하세요:");
		idString = scanner.nextLine();
		System.out.println("비밀번호를 입력하세요:");
		pwString = scanner.nextLine();
		String sql1 = "select * from memtest where id=?";
		String sql2 = "delete from memtest where id=?";
		try(PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);){
			ps1.setString(1, idString);
			try(ResultSet rs = ps1.executeQuery()){
				if(rs.next()) {
					String id=rs.getString("id");
					String pw=rs.getString("pass");
					if(idString.equals(id) && pwString.equals(pw)) {
						ps2.setString(1, idString);
						ps2.executeUpdate();
						System.out.println("성공!");
					}
					else {
						System.out.println("아이디나 비밀번호가 틀립니다.");
						return false;
					}
				}
				else {
					System.out.println("해당하는 아이디가 없습니다");
					return false;
				}
			}
			Iterator<Member> iter = members.iterator();
			while(iter.hasNext()) {
				Member member = iter.next();
				if(member.getId().equals(idString) && member.getPw().equals(pwString)) {
					iter.remove();
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 회원 리스트 출력
	 */
	@Override
	public void listMember(List<Member> members) {
		
		String sql = "select * from memtest";
		try(PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();){
			while(rs.next()) {
				int num = rs.getInt("Mnum");
				String id = rs.getString("id");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String addr = rs.getString("addr");
				String pw = rs.getString("pass");
				Member member = new Member(num,id,name,phone,addr,pw);
				System.out.println(member);
			}

		for (Member m : members) { // 회원 db 하나씩 돌면서
			// 회원 정보 출력
			System.out.println("회원번호 " + m.getNum() + "  이름 " + m.getName() + "  연락처 " + m.getPhone());
		}
		}catch (SQLException e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void showOrders() {
		String sql = "select memtest.MNum, memtest.id, memtest.name as mem_name, memtest.addr, " +
	             "items.name as item_name, orders.ONum, orders.stock, orders.totalprice " +
	             "from orders " +
	             "inner join memtest on orders.FMNum = memtest.MNum " +
	             "inner join items on orders.FINum = items.INum";
		try(PreparedStatement ps = conn.prepareStatement(sql)){
			System.out.printf("%-10s%-8s%-8s%-8s%-8s%-8s%-8s%-8s%n",
	                "주문번호", "상품명", "주문수량", "주문가격", "유저번호", "유저아이디", "유저이름", "유저주소");
			try(ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					int mNum = rs.getInt("MNum");
					String id = rs.getString("id");
					String mem_name = rs.getString("mem_name");
					String addr = rs.getString("addr");
					String item_name = rs.getString("item_name");
					int oNum = rs.getInt("ONum");
					int stock = rs.getInt("stock");
					int totalprice = rs.getInt("totalprice");
					System.out.printf("%-10d%-10s%-10d%-10d%-10d%-10s%-10s%-10s%n",
			                oNum, item_name, stock, totalprice, mNum, id, mem_name, addr);
				}
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 회원 정보 파일로 만들기
	 */
	@Override
	public boolean fileMmeber(List<Member> members) {

		File file = new File("members.txt"); // 파일 객체 생성, 파일 이름을 생성자 매개변수로

		try {
			if (!file.exists()) { // 같은 파일이 존재하지 않으면
				file.createNewFile(); // 새로운 파일 생성
			}

			FileWriter fw = new FileWriter(file); // 파일 쓰기 위한 객체
			for (Member m : members) // 멤버 하나씩 돌면서
				// 해당 회원의 정보를 씀(파일로 내보냄)
				fw.write("회원번호 : " + m.getNum() + ", 아이디 : " + m.getId() + ", 이름 : " + m.getName() + ", 연락처 : "
						+ m.getPhone() + ", 주소 : " + m.getAddr() + ", 비밀번호 : " + m.getPw() + "\n");
			fw.close(); // 객체 닫아주기
			System.out.println("파일 읽기 완");
			//ms = members;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void showItems() {
		
		String sql = "select * from items";
		System.out.printf("%-7s%-7s%-7s%-7s%n", "상품번호", "상품이름", "상품수량", "상품가격");
		try(PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs= ps.executeQuery()){
			while(rs.next()) {
				int iNum = rs.getInt("INum");
				String name = rs.getString("name");
				int stock = rs.getInt("stock");
				int price = rs.getInt("price");
				System.out.printf("%-9d%-7s%-9d%-7d%n", iNum, name, stock, price);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}