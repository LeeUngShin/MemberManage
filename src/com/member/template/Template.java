package com.member.template;

import java.util.Scanner;

public class Template {
	
	Scanner scanner = new Scanner(System.in);
	
	public int selectMode() {
		int cnt=0, mode=0;
		boolean chance_3= true;
		System.out.println("첫화면");
		while(cnt<3) {
			System.out.print("모드 선택(1. 관리자 모드, 2. 일반 회원 모드, 3. 시스템 종료) : ");
			try {
			mode=scanner.nextInt();
			if(!(mode == 1 || mode == 2 || mode==3)) {
				System.out.println("1~3사이의 숫자를 입력하세요.");
				cnt++;
				if(cnt==3) return 4;
				continue;
			}
			cnt++;
			break;
			}catch (Exception e) {
				System.out.println("1~3사이의 숫자를 입력하세요.");
				scanner.nextLine();
				cnt++;
				continue;
			}
		}
		return mode;
	}
	
	public void adminMenu() {
		System.out.println("*******************************************");
		System.out.println("\t\t회원 관리 프로그램");
		System.out.println("*******************************************");
		System.out.println("1. 고객 정보 등록하기\t2. 고객 정보 조회하기");
		System.out.println("3. 고객 정보 수정하기\t4. 고객 정보 삭제하기");
		System.out.println("5. 고객 정보 목록보기\t6. 고객 정보 파일출력");
		System.out.println("7. 종료");
		System.out.println("*******************************************");
		System.out.print("메뉴 번호를 선택해주세요");
	}

}
