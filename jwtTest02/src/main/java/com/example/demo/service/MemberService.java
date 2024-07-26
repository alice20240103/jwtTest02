package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.MemberDTO;
import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;


// 회원에 대한 비지니스로직 처리를 위한 MemberService 클래스를 만들어 준다.
@Service
public class MemberService {
	
	  // 데이터베이스 연결을 위한 MemberRepository를 주입받는다.
	 @Autowired
	 private MemberRepository memberRepository;

	 	// 암호 인코딩을 위한 PasswordEncoder를 주입받는다.
	    @Autowired
	    private PasswordEncoder passwordEncoder;

	    // 회원등록을 위한 메소드
	    // 뷰에서 입력한 정보를 DTO로 받아온다.
	    public Member registerMember(MemberDTO memberDTO) throws Exception {
	    	
	    	// 해당회원이 이미 데이터베이스에 존재한다면 예외를 발생
	        if (memberRepository.existsById(memberDTO.getId())) {
	            throw new Exception("Username is already taken.");
	        }

	        // 데이터베이스 insert을 위하여 Entity객체에 담아준다.
	        Member member = new Member();
	        member.setId(memberDTO.getId());
	        member.setUsername(memberDTO.getUsername());
	        member.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
	        member.setRole("ROLE_USER");  // 기본 역할 설정

	        //데이터베이스에 등록후 insert한 회원의 정보를 받아와서 반환한다.
	        Member createdUser= memberRepository.save(member);
	        return createdUser;
	    }

	    // id를 매개변수로 받아 해당해원의 정보를 조회하여 반환한다.
		public Member findById(String id) {
			return memberRepository.findById(id).get();
		}
}
