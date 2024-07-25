package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.security.JwtUtil;

@RestController
public class AuthController {
	@Autowired
    private AuthenticationManager authenticationManager;
	//인증처리를 위해서 스프링시큐리티가 제공하는 객체

    @Autowired
    private JwtUtil jwtUtil;
    //로그인 성공 후 세션 대신 JWT 토큰을 발행하기 위한 JwtUtil 객체
    
    
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
    	//post 방식의 login요청시에 아이디와 암호를 매개변수로 전달받는다.
    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        // 사용자가 입력한 아이디과 암호를 매개변수로 전달하여 Authentication 객체를 생성하여 검증
        // UserDetailsServiceImpl의 loadUserByUsername이 자동으로 호출됨

        if (authentication.isAuthenticated()) { 
        	// 올바른 회원이면(인증에 성공하면)        	
            return jwtUtil.generateToken(username, "user"); 
            // 아이디와 role를 매개변수르 전달하여 JWT 토큰을 생성하여 반환한다.
        } else {
            throw new RuntimeException("Authentication failed"); 
            //올바른 회원이 아니면 예외를 발생시킨다.
        }
    }    
}	
