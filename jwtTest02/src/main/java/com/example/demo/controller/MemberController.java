package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.MemberDTO;
import com.example.demo.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberController {
	 @Autowired
	 private MemberService memberService;	 

	@PostMapping("/register")
	 public String registerMember(MemberDTO memberDTO) {
		System.out.println("register 컨트롤러 동작함");
        try {
            memberService.registerMember(memberDTO);
            return "User registered successfully.";
        } catch (Exception e) {
            return "error";
        }
    }
}
