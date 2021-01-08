package com.ecommerce;

import java.util.Date;
import java.util.List;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenGenerator {

	public static String getToken(Long userId,String role,String secret, long expiration) {
		String token = Jwts.builder()
				.setSubject(String.valueOf(userId))
				.claim("ROLE", List.of("ROLE_"+role))
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
		return token;
	}
	
}
