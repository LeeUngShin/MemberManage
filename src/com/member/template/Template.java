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


}
