//package com.login.main;
//
//import java.util.Scanner;
//
//import com.member.controller.UserMenu;
//
//public class MemberMain {
//
//	public static void main(String[] args) {
//
//		Scanner scanner = new Scanner(System.in);
//
//		int num = 1;
//
//		boolean executing=true;  // false시 프로그램 실행 종료
//		
//		// 일반 회원 기능 모아놓은 클래스
//		UserMenu loginMenu = new UserMenu();
//		
//		
//		loginMenu.readFileMember();  // 회원 db 저장
//
//		boolean login = loginMenu.login(); // 로그인 성공 시 true 반환
//
//		if (login) {  // 로그인 성공 시 메뉴 선택 화면으로 이동
//			while (executing) {
//				loginMenu.menu();  // 메뉴 선택 화면
//				num = scanner.nextInt();  // 메뉴 번호 입력
//				switch (num) {
//				case 1:
//					loginMenu.readMember();  // 해당 회원정보 조회
//					break;
//				case 2:
//					boolean result = loginMenu.updateMember();  // 회원 정보 수정
//					if (result)
//						System.out.println("수정 완료되었습니다.");
//					break;
//				case 3:
//					boolean result2 = loginMenu.deleteMember();  // 회원 탈퇴
//					if(result2==true) {
//						System.out.println("탈퇴 완료되었습니다.");
//						System.out.println("로그인 프로그램을 종료합니다.");
//						executing = false;  //
//					}
//					break;
//				case 4:
//					System.out.println("종료합니다.");
//					executing=false;
//					break;
//				default:
//					System.out.println("1~4사이의 번호를 입력하세요");
//				}
//			}
//		}
//	}
//}