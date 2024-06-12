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

public class AdminMenu implements ManageMember {

	Scanner scanner = new Scanner(System.in);
	static int num = 2;
	String idString;
	String nameString;
	String phoneString;
	String addrString;
	String pwString;
	int cnt = 0;
	private List<Member> ms = new ArrayList<Member>();

	private DbEx dbEx = new DbEx();

	/**
	 * 관리자 계정 생성
	 * 
	 * @return
	 */
	public List<Member> createAdmin(List<Member> members) {
		String sql = "INSERT INTO memtest(id, name, phone, addr, pass) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = dbEx.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			// 데이터베이스 연결 생성

			// PreparedStatement에 값 설정
			ps.setString(1, "관리자");
			ps.setString(2, "admin");
			ps.setString(3, "01000000000");
			ps.setString(4, "관리자주소");
			ps.setString(5, "관리자");

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

	/**
	 * 로그인 화면 및 기능
	 * 
	 * @return
	 */
	public boolean adminLogin(List<Member> members) {
		while (cnt < 3) { // 3번 초과 시 프로그램 종료
			System.out.println("*******************************************");
			System.out.println("\t\t로그인");
			System.out.println("*******************************************");
			System.out.print("아이디를 입력하세요:");
			String id = scanner.nextLine();
			if (!(members.get(0).getId().equals(id))) { // 내가 입력한 id와 관리자 id 같지 않으면
				System.out.println("일치하는 아이디가 없습니다.");
				cnt++; // 실패 횟수 증가
				continue; // 다시 로그인 화면으로 이동
			}
			System.out.print("비밀번호를 입력하세요:");
			String pw = scanner.nextLine();
			if (!(members.get(0).getId().equals(pw))) { // 내가 입력한 pw와 관리자 pw 같지 않으면
				System.out.println("비밀번호가 틀렸습니다.");
				cnt++; // 실패 횟수 증가
				continue; // 다시 로그인 화면으로 이동
			}
			// 내가 입력한 id와 관리자 id가 같고 내가 입력한 pw와 pw가 같으면
			if ((members.get(0).getId().equals(id)) && (members.get(0).getPw().equals(pw))) {
				System.out.println("로그인 성공");
				return true; // true 반환
				// break;
			}
		}
		// ctn가 3이 되면 while()문 나와서
		System.out.println("로그인 횟수 초과");
		return false; // false 반환
	}

	/**
	 * 회원 등록
	 */
	@Override
	public boolean createMmeber(List<Member> members) throws MyException {
		String sql = "INSERT INTO memtest(id, name, phone, addr, pass) VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = dbEx.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
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
			System.out.println(members);
			System.out.println("회원등록 후 : " + members);
		} catch (SQLException e) {
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
		String sql = "select * from memtest where num = ?";

		try (Connection conn = dbEx.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {
			ps.setLong(1, num);
			// 쿼리 실행
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					int num1 = rs.getInt("num");
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
								int num1 = rs2.getInt("num");
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
		try (Connection conn = dbEx.getConnection();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
				PreparedStatement ps2 = conn.prepareStatement(sql2);) {
			ps2.setString(1, idString);
			try (ResultSet rs2 = ps2.executeQuery()) {
				if(rs2.next()) {
					int num1 = rs2.getInt("num");
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
		System.out.println("정보수정후 : " + members);
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
		System.out.println("삭제전 : "+members);
		System.out.println("삭제할 회원의 아이디를 입력하세요:");
		idString = scanner.nextLine();
		System.out.println("비밀번호를 입력하세요:");
		pwString = scanner.nextLine();
		String sql1 = "select * from memtest where id=?";
		String sql2 = "delete from memtest where id=?";
		try(Connection conn = dbEx.getConnection();
				PreparedStatement ps1 = conn.prepareStatement(sql1);
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
			
			System.out.println("삭제 후 : "+members);
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
		try(Connection conn = dbEx.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery();){
			while(rs.next()) {
				int num = rs.getInt("num");
				String id = rs.getString("id");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String addr = rs.getString("addr");
				String pw = rs.getString("pass");
				Member member = new Member(num,id,name,phone,addr,pw);
				System.out.println((num+1)+"번쨰 회원 : " + member);
			}

		for (Member m : members) { // 회원 db 하나씩 돌면서
			// 회원 정보 출력
			System.out.println("회원번호 " + m.getNum() + "  이름 " + m.getName() + "  연락처 " + m.getPhone());
		}
		}catch (SQLException e) {
			// TODO: handle exception
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
			ms = members;
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Member> getList(){
		return ms;
	}

	@Override
	public boolean readMmeber() {
		// TODO Auto-generated method stub
		return false;
	}
}