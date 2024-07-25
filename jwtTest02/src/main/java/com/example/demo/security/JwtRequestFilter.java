package com.example.demo.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtRequestFilter extends OncePerRequestFilter {
// 로그인한 사용자인지 인증을 위하여 시큐리티의 OncePerRequestFilter를 구현한 클래스를 만들어 준다.
// 시큐리티 설정파일의 permitAll 이외의 모든 요청일때에 가로채어 이 클래스가 동작한다.
	
	@Autowired
    private JwtUtil jwtUtil;
	// 만약, 로그인을 성공한 사람이라면 JWT토큰을 발행했을테니 그것을 확인하여 위한 객체를 생성한다.
	
	@Autowired
    private UserDetailsServiceImpl userDetailsService;
	// 로그인한 회원이면 회원의 정보를 알아오기 위하여 UserDetailsServiceImpl 객체를 생성
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain) throws ServletException, IOException {
	//실제로 필터가 수행하는 메소드	
		
	String token = request.getHeader("Authorization");
	// request의 header로 부터 인증정보를 갖고 온다.
	
    if (token != null && token.startsWith("Bearer ")) {
    	// 인증정보가 있고 그 인증정보보의 시작이 내가 설정한 것이 맞다면
    	
        token = token.substring(7);
        // 앞에서 7자리를 잘라온다.
        
        String username = jwtUtil.getUsernameFromToken(token);
        // 토큰 발행자를 알아온다.
        
        if (username != null && 
        		SecurityContextHolder.getContext().getAuthentication() == null) {
        		// 토큰발행자가 null이 아니고 인증된 상태라면
        	
            		if (jwtUtil.validateToken(token, username)) {
            		// 올바른 토큰이라면	
            			
            			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            			// 로그인한 회원의 정보를 받아온다.
            			
            			SecurityContextHolder.getContext().setAuthentication(
            					new UsernamePasswordAuthenticationToken(
            							userDetails, 
            							null, 
            							userDetails.getAuthorities()));
            			//로그인한 회원의 정보를 어플리케이션에 설정한다.
            }
        }
    }
    
    filterChain.doFilter(request, response);
    //요청한 곳으로 보낸다.
    
	}
}
