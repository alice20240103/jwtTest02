package com.example.demo.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(String username, String role) {
        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(secretKey));
    }

    public String getUsernameFromToken(String token) {
        return JWT.decode(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return JWT.decode(token).getClaim("role").asString();
    }

    public boolean validateToken(String token, String username) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey))
                .build();
        try {
            DecodedJWT decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject().equals(username);
        } catch (Exception e) {
            return false;
        }
    }
}
