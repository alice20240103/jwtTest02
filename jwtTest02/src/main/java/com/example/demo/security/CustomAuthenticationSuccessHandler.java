package com.example.demo.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// AuthenticationSuccessHandler : Spring Security에서 로그인 성공 후 특정 동작을 수행하기 위해 사용
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	//로그인 성공 시 onAuthenticationSuccess 메소드가 자동으로 호출됨.
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// 로그인 성공 후 사용자를 특정 URL로 리다이렉트
		response.sendRedirect("/");
	}
}
