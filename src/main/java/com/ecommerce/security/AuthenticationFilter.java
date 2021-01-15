package com.ecommerce.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authManger;
	private String secret;
	private long expiration;


	public AuthenticationFilter(AuthenticationManager authManger, SecurityConfig config) {
		this.authManger = authManger;
		this.secret = config.getSecret();
		this.expiration = config.getExpiration();
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		Authentication authentication = null;
		try {
			LoginDTO loginDTO = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					loginDTO.getEmailId(), loginDTO.getPassword());
			authentication = authManger.authenticate(authenticationToken);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return authentication;
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		List<String> authorities = authResult.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
		UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
		Long userId = userDetails.getUser().getId();
		String token = Jwts.builder()
						.setSubject(userId.toString())
						.claim("ROLE", authorities)
						.setIssuedAt(new Date())
						.setExpiration(new Date(System.currentTimeMillis() + expiration))
						.signWith(SignatureAlgorithm.HS512, secret.getBytes())
						.compact();
		response.addHeader("auth", token);
		response.getOutputStream().print("Success");
	}
	
	
}
