package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.Setter;


// 스프링시큐리티 설정을 위한 클래스
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	// 인증된 회원의 정보를 읽어오기 위한 스프링시큐리티가 제공하는  UserDetailsService 객체
	// Spring Security의 객체로, 사용자 정보를 로드하는 데 사용됩니다.
	private final UserDetailsService userDetailsService;
    
	// 인증된 회원인지 판별하기 위한 사용자 정의 JwtRequestFilter 객체
	// JWT 인증을 수행하는 커스텀 필터입니다. 인증이 필요한 요청을 가로채서 JWT 검증을 수행합니다.
	private final JwtRequestFilter jwtRequestFilter;

	// 생성자에서 맴버변수들을 생성한다.
	// 생성자에서 UserDetailsService와 JwtRequestFilter 객체를 주입받습니다. 이를 통해 Spring Security 설정에 필요한 객체를 초기화합니다.
    public SecurityConfig(UserDetailsService userDetailsService, JwtRequestFilter jwtRequestFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtRequestFilter = jwtRequestFilter;
    }
	

	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	
		// "/","/login", "/register", "/api/register", "/users/**","users") 에대한 요청은
		// 인증과 관계없이 접근할 수 있고
		// 나머지 서비스들은 로그인을 해야 접근할 수 도 있도록 설정한다. 
		// 또, 인증되어야지만 접근할 수 있는 요청을 가로 채어 jwtRequestFilter가 동작하도록 한다.
    	http
         .authorizeHttpRequests((requests) -> requests
             .requestMatchers("/","/login", "/register", "/api/register", "/users/**","users").permitAll()
             .anyRequest().authenticated()
         )
         .formLogin()
         .loginPage("/login") // 로그인 페이지 URL 설정
         .successHandler(customAuthenticationSuccessHandler()) // 로그인 성공 핸들러 설정
	     .and()
	     .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
         
    	
    	return http.build();
	}    	

	
	// 로그인 성공하면 이동할 리다이렉트 설정을 위한 
	// 사용자 정의 CustomAuthenticationSuccessHandler 객체를 생성한다.
	 @Bean
	 public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
	        return new CustomAuthenticationSuccessHandler();
	 }
	
	// 스프링 시큐리트를 위하여  AuthenticationManager 객체를 생성한다.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    
    // 회원가입이나 로그인시 비빌번호를 암호화하기 위한 객체를 생성한다.
    // 스프링시큐리티 환경에서는 반드시 비밀번호를 암호화 해야 한다.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    
}
