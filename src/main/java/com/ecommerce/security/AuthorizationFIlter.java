package com.ecommerce.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

public class AuthorizationFIlter extends OncePerRequestFilter {
	
	private String secret;

	public AuthorizationFIlter(String secret) {
		this.secret = secret;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		if(authHeader == null || authHeader.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = authHeader.replace("Bearer", "");
		Jws<Claims> claims = Jwts.parser()
				.setSigningKey(secret.getBytes())
				.parseClaimsJws(token);
		Map<String,Object> claimsMap = claims.getBody();
		String username = (String) claimsMap.get("sub");
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(String authority :  (List<String>) claimsMap.get("ROLE")) {
			authorities.add(new SimpleGrantedAuthority(authority));
		}
		Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication );
		filterChain.doFilter(request, response);
	}

}
