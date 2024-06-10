package com.member.controller;

import java.util.List;

import com.member.domain.Member;
import com.member.exception.MyException;

public interface ManageMember {
	abstract boolean createMmeber(List<Member> members) throws MyException;
	abstract boolean readMmeber(int num, List<Member> members);
	abstract boolean readMmeber();
	abstract boolean updateMmeber(List<Member> members);
	abstract boolean deleteMmeber(List<Member> members);
	abstract boolean fileMmeber(List<Member> members);
	abstract void listMember(List<Member> members);
}