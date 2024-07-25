package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.MemberDTO;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {
	 @Autowired
	    private MemberRepository memberRepository;

	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    public void registerMember(MemberDTO memberDTO) throws Exception {
	        if (memberRepository.existsById(memberDTO.getId())) {
	            throw new Exception("Username is already taken.");
	        }

	        Member member = new Member();
	        member.setId(memberDTO.getId());
	        member.setUsername(memberDTO.getUsername());
	        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
	        member.setRole("ROLE_USER");  // 기본 역할 설정

	        memberRepository.save(member);
	    }
}
