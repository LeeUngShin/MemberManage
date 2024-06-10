package com.member.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.member.domain.Member;
import com.member.exception.MyException;

public class AdminMenu implements ManageMember {

	Scanner scanner = new Scanner(System.in);
	int num = 1;
	String idString;
	String nameString;
	String phoneString;
	String addrString;
	String pwString;
	int cnt = 0;

	/**
	 * 관리자 계정 생성
	 * 
	 * @return
	 */
	public List<Member> createAdmin(int num, List<Member> members) {
		// 번호, 아이디, 이름, 전화번호, 주소, 비밀번호
		members.add(new Member(num, "admin", "관리자", "01011112222", "관리자주소", "admin"));
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
		for (Member m : members) {// 회원 db를 하나씩 가져오면서
			if (m.getId().equals(idString)) { // 내가 입력한 id와 회원 id가 같으면
				throw new MyException("동일한 아이디가 존재합니다."); // 예외 던지고 메서드 종료
			}
		}
		// 동일한 id가 없으면 회원 정보 등록
		members.add(new Member(num, idString, nameString, phoneString, addrString, pwString));
		num++; // 다음 회원의 회원 번호 1 증가 시키기 위해
		System.out.println(members);
		return true;
	}

	/**
	 * 회원 정보 조회
	 */
	@Override
	public boolean readMmeber(int num, List<Member> members) {
		// System.out.println(members);

		for (Member m : members) {
			if (num == m.getNum()) {
				System.out.println("일치하는 회원번호가 있습니다.");
				System.out.println(m);
				return true;
			}
		}
		System.out.println("해당하는 회원번호의 회원이 없습니다.");
		return false;
	}

	/**
	 * 회원 정보 수정
	 */
	@Override
	public boolean updateMmeber(List<Member> members) {
		System.out.println("수정할 회원 아이디를 입력해주세요.");
		idString = scanner.nextLine();
		for (Member m : members) { // 회원 db 처음부터 돌면서
			if (m.getId().equals(idString)) { // 내가 입력한 id와 회원 id가 같은 경우를 만나면 그 회원의 정보 수정
				System.out.println(m.getId() + " 회원의 이름을 수정하세요:");
				nameString = scanner.nextLine();
				System.out.println(m.getId() + " 회원의 연락처를 수정하세요:");
				phoneString = scanner.nextLine();
				System.out.println(m.getId() + " 회원의 주소를 수정하세요:");
				addrString = scanner.nextLine();
				System.out.println(m.getId() + " 회원의 비밀번호를 입력하세요");
				pwString = scanner.nextLine();
				if (!(m.getPw().equals(pwString))) { // 입력한 pw와 해당 회원의 pw가 같지 않으면
					System.out.println("비밀번호 불일치로 회원정보 수정 실패");
					return false; // 정보 수정 실패
				}
				// 비밀번호가 같으면
				if (nameString.equals("")) // 아무 입력 안하면
					m.setName(m.getName()); // 원래 이름 저장
				else // 입력하면
					m.setName(nameString); // 입력한 이름 저장
				if (phoneString.equals("")) // 아무 입력 안하면
					m.setPhone(m.getPhone()); // 원래 번호 저장
				else // 입력하면
					m.setPhone(phoneString); // 입력한 번호 저장
				if (addrString.equals("")) // 아무 입력 안하면
					m.setAddr(m.getAddr()); // 원래 주소 저장
				else // 입력하면
					m.setAddr(addrString); // 입력한 주소 저장
				return true; // 수정 성공 시 true 반환
			}
		}
		// 입력한 id와 동일한 회원이 없으면
		System.out.println("일치하는 아이디가 없습니다.");
		return false;
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
		for (Member m : members) { // 회원 db 하나씩 돌면서
			// 내가 입력한 id와 회원 id가 같고 내가 입력한 pw와 회원 pw가 같은 경우가 있으면
			if ((m.getId().equals(idString)) && m.getPw().equals(pwString)) {
				members.remove(m); // 해당 회원을 db에서 삭제
				return true; // true 반환
			}
		}
		// 내가 입력한 id와 회원 id가 같고 내가 입력한 pw와 회원 pw가 같은 경우가 없으면
		System.out.println("일치하는 아이디와 비밀번호가 없습니다.");
		return false;
	}
	
	/**
	 * 회원 리스트 출력
	 */
	@Override
	public void listMember(List<Member> members) {
		for (Member m : members) { // 회원 db 하나씩 돌면서
			// 회원 정보 출력
			System.out.println("회원번호 " + m.getNum() + "  이름 " + m.getName() + "  연락처 " + m.getPhone());
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
				fw.write("회원번호 : " + m.getNum()+", 아이디 : " + m.getId()+", 이름 : " + m.getName() + ", 연락처 : "
						+ m.getPhone() + ", 주소 : " + m.getAddr() + ", 비밀번호 : " + m.getPw() + "\n");
			fw.close(); // 객체 닫아주기
			System.out.println("파일 읽기 완");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void returnMode() {
		
	}

	@Override
	public boolean readMmeber() {
		// TODO Auto-generated method stub
		return false;
	}

}