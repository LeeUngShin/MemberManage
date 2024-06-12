package com.member.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.db.DbEx;
import com.member.controller.AdminMenu;
import com.member.controller.UserMenu;
import com.member.domain.Member;
import com.member.exception.MyException;
import com.member.template.Template;

public class MemberMain {

	public static void main(String[] args) {

		List<Member> members = new ArrayList<>();
		DbEx dbEx = new DbEx();
		dbEx.initTable();

		Scanner scanner = new Scanner(System.in);

		AdminMenu adminMenu = new AdminMenu(); // 관리자 기능 모아져 있는 클래스
		UserMenu userMenu = new UserMenu(members); // 회원 기능 모아져 있는 클래스
		adminMenu.createAdmin(members);

		System.out.println("관지자 정보 : " + members); // 관리자 계정 생성

		boolean adminLoginSuccess = false;
		boolean userLoginSuccess = false;

		Template template = new Template();

		while (true) {
			int mode = template.selectMode(); // 1->관리자, 2->회원, 3->기회초과

			if (mode == 1) { // 관리자모드
				adminLoginSuccess = adminMenu.adminLogin(members); // 로그인 화면 - 성공 시 true 반환
				System.out.println("관리자 로그인 성공여부 : " + adminLoginSuccess);
			} else if (mode == 2) { //
				userLoginSuccess = userMenu.login();
				System.out.println("일반 회원 로그인 성공여부 : " + userLoginSuccess);
			} else if (mode == 3) {
				System.out.println("프로그램을 종료합니다.");
				break;
			}
			else {
				System.out.println("입력 횟수 초과로 프로그램을 종료합니다.");
				break;
			}

			boolean adminLogining = true;
			if (adminLoginSuccess) { // 로그인 성공 시 메뉴화면 출력
				while (adminLogining) {
					template.adminMenu(); // 메뉴화면 출력 메서드
					try {
					int num = scanner.nextInt(); // 메뉴번호 입력
					switch (num) {
						case 1: // 1번 메뉴 선택
							boolean result1;
							try {
								result1 = adminMenu.createMmeber(members); // 회원 등록 메서드 - 등록 성공 시 true 반환
								if (result1 == true) // 회원 등록 성공 시
									System.out.println("등록완료되었습니다.");
							} catch (MyException e) { // 회원 등록 중 예외발생 시(동일한 아이디가 존재)
								System.out.println(e.getMessage());
							}
							break;
						case 2:
							System.out.print("조회할 회원번호 : ");
							int userNum = scanner.nextInt();
							adminMenu.readMmeber(userNum); // 회원 정보 조회(아이디로)
							break;
						case 3:
							boolean result3 = adminMenu.updateMmeber(members); // 회원 정보 수정
							if (result3 == true) // 수정 성공 시
								System.out.println("수정 완료되었습니다.");
							break;
						case 4:
							boolean result4 = adminMenu.deleteMmeber(members); // 회원 정보 삭제
							if (result4 == true) // 삭제 성공 시
								System.out.println("삭제되었습니다.");
							break;
						case 5:
							adminMenu.listMember(members); // 회원 리스트 출력
							break;
						case 6:
							boolean result6 = adminMenu.fileMmeber(members); // 모든 회원 정보 파일(.txt)로 만들기
							break;
						case 7:
							System.out.println("회원 관리 프로그램을 종료합니다.");
							adminLogining = false;
							adminLoginSuccess = false;
							break;
						default:
							System.out.println("1부터 7까지의 숫자를 입력하세요.");
					}
					}catch (Exception e) {
						System.out.println("1~7사이의 숫자를 입력하세요");
						scanner.nextLine();
						System.out.println(e.getMessage());
						e.printStackTrace();
						continue;
					}
				}
			}
			if (adminLogining == false && adminLoginSuccess == false)
				continue;

			boolean userLogining = true;
			if (userLoginSuccess) {
				while (userLogining) {
					userMenu.menu(); // 메뉴 선택 화면
					try {
					int menuNum = scanner.nextInt(); // 메뉴 번호 입력
					switch (menuNum) {
						case 1:
							userMenu.readMember(); // 해당 회원정보 조회
							break;
						case 2:
							boolean result = userMenu.updateMember(); // 회원 정보 수정
							if (result)
								System.out.println("수정 완료되었습니다.");
							else {
								System.out.println("수정 실패");
							}
							break;
						case 3:
							boolean result2 = userMenu.deleteMember(); // 회원 탈퇴
							if (result2 == true) {
								System.out.println("탈퇴 완료되었습니다.");
								System.out.println("로그인 프로그램을 종료합니다.");
								userLogining = false;
								userLoginSuccess = false;
							}
							break;
						case 4:
							System.out.println("종료합니다.");
							userLogining = false;
							break;
						default:
							System.out.println("1~4사이의 번호를 입력하세요");
					}
					}catch (Exception e) {
						System.out.println("1~4사이의 숫자를 입력하세요");
						scanner.nextLine();
						continue;
					}
				}
			}
			if(userLoginSuccess==false&&userLogining==false) continue;
		}
	}
}