package com.member.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.member.domain.Member;
import com.member.exception.MyException;

public class UserMenu implements UserLogin {

	String id;
	String pw;
	Scanner scanner = new Scanner(System.in);
	int cnt = 0; // 로그인 실패 횟수
	boolean login = false; // 로그인 성공 여부
	List<Member> members = new ArrayList<>(); // 회원 db 저장
	Member member = new Member();  // 로그인한 회원의 정보 저장
	String[] arr = new String[20];  // 파일 읽어와서 그 파일의 텍스트를 split()으로 쪼개서 배열에 저장

	public UserMenu() {
		// TODO Auto-generated constructor stub
	}

	public UserMenu(List<Member> members) {
		this.members = members;
		System.out.println("생성자로 받은 후 : " + members);
	}

	/**
	 * 로그인 화면+기능
	 * @return
	 */
	public boolean login() {
		System.out.println(members);
		System.out.println("************************************************");
		System.out.println("\t\t로그인");
		System.out.println("************************************************");
		while (cnt < 3) {
			boolean idFound = false;
			System.out.println("아이디를 입력하세요: ");
			id = scanner.nextLine();
			System.out.println("비밀번호를 입력하세요.");
			pw = scanner.nextLine();

			for (Member member : members) { // 회원 db 돌면서
				if (member.getId().equals(id)) { // 입력 id와 회원 id가 같은 경우를 만나고
					idFound = true;
					if (member.getPw().equals(pw)) { // 입력 pw와 회원 pw가 같으면
						System.out.println("로그인 성공");
						this.member = member; // 로그인 성공한 회원정보를 member객체에 저장
						return true; // true 반환
					} else { // id는 같은데 pw가 틀렸으면
						System.out.println("비밀번호가 틀렸습니다.");
						cnt++; // 로그인 실패 횟수 증가
						break;
					}
				}
			}
			// 회원 db 다 돌아도 일치하느 아이디가 없으면, 일치하는 아이디가 있으면 idFound가 true가 돼서 이 if문 들어오지 않음
			if (!idFound) {
				System.out.println("일치하는 아이디가 없습니다.");
				cnt++; // 로그인 실패횟수 증가
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
		System.out.println(" 3. 회원 탈퇴\t\t 4. 종료");
		System.out.println("메뉴 번호를 선택해 주세요.");

	}

	/**
	 * 회원 정보 조회
	 */
	@Override
	public void readMember() {
		System.out.println(member.getId() + " 고객 정보");
		System.out.println("회원번호 " + member.getNum() + "  이름 " + member.getName() + "  연락처 " + member.getPhone()
				+ "  주소 " + member.getAddr());
		System.out.println(members);
	}

	/**
	 * 회원 정보 수정
	 * 
	 * @throws MyException
	 */
	@Override
	public boolean updateMember() {
		while (true) {
			try {
				System.out.println(member.getId() + "회원님의 이름을 수정하세요.");
				String name = scanner.nextLine();
				System.out.println(member.getId() + "회원님의 연락처를 수정하세요.");
				String phone = scanner.nextLine();
				System.out.println(member.getId() + "회원님의 주소를 수정하세요.");
				String addr = scanner.nextLine();
				System.out.println(member.getId() + "회원님의 비밀번호를 입력하세요.");
				String pw = scanner.nextLine();

				if (!(member.getPw().equals(pw))) { // 입력 pw와 로그인 한 회원은 pw가 같지 않으면
					System.out.println("비밀번호가 일치하지 않습니다.");
					return false; // 정보 수정 실패 시 false 반환
				}
				for (Member m : members) { // 회원 db 돌면서
					if (m.getName().equals(name)) { // 입력 이름과 같은 이름인 회원이 있으면
						throw new MyException("이미 존재하는 이름입니다. 다른 이름으로 수정해주세요."); // 예외 발생
					}
					if (name.equals(""))  // 아무것도 입력안하면
						member.setName(member.getName());  // 원래 데이터 넣기
					else  				// 입력하면
						member.setName(name);  // 입력한 이름을 수정
					if (phone.equals(""))
						member.setPhone(member.getPhone());
					else
						member.setPhone(phone);
					if (addr.equals(""))
						member.setAddr(member.getAddr());
					else
						member.setAddr(addr);
					System.out.println(members);
					writeFileMember();  // 수정한 회원 정보로 파일 수정
					return true;  // 수정 완료 시 true 반환
				}
			} catch (MyException e) {  // 동일 이름의 회원이 있으면 예외 발생해 이쪽으로 넘어오고
				System.out.println(e.getMessage());  // "이미 존재하는 이름입니다. 다른 이름으로 수정해주세요." 메세지 출력
				System.out.println("동일 이름 존재하는 데 수정하려고해 : " + members);
				continue;  // 수정할 데이터 다시 입력하러 while() 다시 실행할러 감
			}
		}
	}

	/**
	 * 회원 탈퇴
	 */
	@Override
	public boolean deleteMember() {
		System.out.println("비밀번호를 입력하세요");
		String pw = scanner.nextLine();
		if (member.getPw().equals(pw)) {  // 입력 pw와 로그인한 회원 pw가 같으면 
			members.remove(member);  // 해당 회원정보 회원 db에서 삭제
			System.out.println(members);
			writeFileMember();  // 회원 삭제 반영해 파일 수정
			return true;  // 수정 후 true 반환
		} else {  // 입력 pw와 로그인한 회원 pw가 다르면
			System.out.println("비밀번호가 틀렸습니다.");
			return false;  // false 반환
		}
	}
	
	/**
	 * 파일 읽어와 회원 정보 저장
	 */
	@Override
	public void readFileMember() {
		try {
			File file = new File("members.txt");
			if(!(file.exists())) file.createNewFile();
			FileReader fis = new FileReader(file);
			BufferedReader br = new BufferedReader(fis);
			String str;  // 파일에서 읽어온 문자열 저장하기 위한 변수

			while((str=br.readLine()) != null) {  // members2.txt 파일을 한 줄씩 읽어와서
				arr = str.split(", ");  // 공백을 기준으로 배열의 저장
				//System.out.println("현재배열 : "+Arrays.toString(arr));
				String[] arrSplit = new String[2];
				String[] memberInput = new String[6];
				for(int i=0;i<6;i++) {
					arrSplit = arr[i].split(" : ");
					memberInput[i] = arrSplit[1];
					memberInput[i] = memberInput[i].trim();
				}
				Member member = new Member();  // 위 배열에서 데이터만 뽑아서 Member 객체에 저장
				member.setNum(Integer.parseInt(memberInput[0]));
				member.setId(memberInput[1]);
				member.setName(memberInput[2]);
				member.setPhone(memberInput[3]);
				member.setAddr(memberInput[4]);
				member.setPw(memberInput[5]);
				System.out.println("파일 읽어서 : " + member);

//				member.setNum(Integer.parseInt(arr[2]));
//				member.setId(arr[5]);
//				member.setName(arr[8]);
//				member.setPhone(arr[11]);
//				member.setAddr(arr[14]);
//				member.setPass(arr[17]);
				members.add(member);  // 데이터를 저장한 Member 객체를 members 리스트에 저장
				System.out.println("회원db : "+members);
			}
			fis.close();
			System.out.println("파일 읽기 완");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 회원 수정 및 탈퇴 시 반영 해 파일 수정
	 */
	@Override
	public void writeFileMember() {
		File file = new File("members.txt");  // members2.txt 파일 담은 File 객체 
		try {
			if (!(file.exists()))
				file.createNewFile();
			FileWriter fw = new FileWriter(file);
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