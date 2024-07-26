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
	// request의 header로 부터 인증정보(JWT토큰)를 갖고 온다.
	
    if (token != null && token.startsWith("Bearer ")) {
    	// 인증정보가 있고 그 인증정보보의 시작이 내가 설정한 것이 맞다면
    	// JWT 토큰의 표준 형식은 "Bearer "로 시작한다.
    	
        token = token.substring(7);
        // Bearer 접두사를 제거하여 실제 JWT 토큰만 남깁니다. 접두사는 7자이므로 이를 제거합니다.
        
        String username = jwtUtil.getUsernameFromToken(token);
        // 토큰 발행자를 알아온다.
        // jwtUtil을 사용하여 JWT 토큰에서 사용자 이름을 추출합니다.
        
        if (username != null && 
        		SecurityContextHolder.getContext().getAuthentication() == null) {
        		// 토큰발행자가 null이 아니고 인증된 상태라면
        		// 추출한 사용자 이름이 null이 아니고 현재 SecurityContext에 인증이 설정되어 있지 않은지 
        		// 확인합니다. 이는 중복 인증을 방지합니다.
        	
            		if (jwtUtil.validateToken(token, username)) {
            		// 올바른 토큰이라면	
            		// jwtUtil을 사용하여 JWT 토큰의 유효성을 검사합니다. 유효한 토큰인지 확인합니다.
            			
            			UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            			// 로그인한 회원의 정보를 받아온다.
            			// userDetailsService를 사용하여 사용자 이름으로 UserDetails 객체를 로드합니다. 
            			// 이 객체는 인증에 필요한 정보를 담고 있습니다.
            			
            			SecurityContextHolder.getContext().setAuthentication(
            					new UsernamePasswordAuthenticationToken(
            							userDetails, 
            							null, 
            							userDetails.getAuthorities()));
            			// 로그인한 회원의 정보를 어플리케이션에 설정한다.
            			// SecurityContext에 UsernamePasswordAuthenticationToken을 설정하여 
            			// 인증된 사용자의 정보를 저장합니다. 이 객체는 인증된 사용자와 권한 정보를 포함합니다.
            }
        }
    }
    
    filterChain.doFilter(request, response);
    // 요청한 곳으로 보낸다.
    // 다음 필터로 요청을 전달합니다. 이 호출은 필터 체인을 계속 진행하게 합니다.
    
	}
}
