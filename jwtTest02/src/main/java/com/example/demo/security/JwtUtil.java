package com.example.demo.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;


// JWT토큰 발행 및 인증을 위한 기능을 갖고 있는 클래스를 만들어 준다.
@Component
public class JwtUtil {
	
	// application.properties에 설정된 jwt.secret값을 secretKey에 주입한다.
	// 이 비밀 키는 JWT의 서명 및 검증에 사용됩니다.
	@Value("${jwt.secret}")
    private String secretKey;

	
	// username과 role을 매개변수로 받아서 JWT토큰을 발행하여 반환한다.
    public String generateToken(String username, String role) {
        return JWT.create()					// JWT토큰의 생성 	
                .withSubject(username)      // 토큰의 발행자 설정  --> Payload
                .withClaim("role", role)	// 토큰의 정보 설정	   --> Payload
                .sign(Algorithm.HMAC256(secretKey)); // 토큰알고리즘 및 키 설정,--> Header, Signature
    }

    // 클라이언트가 전달한 토큰 문자열을 매개변수로 받아서
    // 디코딩 하여 JWT토큰을 생성한 후 발행자를 알아와서 반환한다.
    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    // 클라이언트가 전달한 토큰 문자열을 매개변수로 받아서
    // 디코딩 하여 JWT토큰을 생성한 후 role을 알아와서 반환한다.
    public String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }

    // 클라이언트가 전달한 토큰 문자열과 발행자 이름을 알아와서 
    // 
    public boolean validateToken(String token, String username) {

    	// 올바른 토큰인지 검증하기 위한 JWTVerifier객체를 생성한다. 
    	// 이때 토큰발행시 사용한 동일한 알고리즘과 키를 사용한다.
    	JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .build();

        try {
        	
        	// 검증객체를 이용하여 매개변수로 전달받은 토큰문자열을 디코딩한다. 
            DecodedJWT decodedJWT = verifier.verify(token);
            
            //디코딩된 JWT객체의 발행자와 사용자가 요청한 발행자가 동일한지 판별하여 반환한다.
            return decodedJWT.getSubject().equals(username);
            
        } catch (Exception e) {
            return false;
        }
    }
}
