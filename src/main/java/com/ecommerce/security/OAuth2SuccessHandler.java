package com.ecommerce.security;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
	
	RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Value("${oauth2.redirect.url}")
	private String redirectURL;
	
	@Value("${token.secret}")
	private String secret;
	
	@Value("${token.expiration}")
	private long expiration;

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder encoder;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
		String email = getOAuth2TokenAttribute(authToken,"email");
		User user = userRepository.findByEmailId(email);
		if(user == null) {
			user = new User();
			user.setEmailId(email);
			
			String fullName = getOAuth2TokenAttribute(authToken,"name");
			String[] splitFullName = fullName.split(" ");
			if(splitFullName.length >1) {
				user.setFirstName(splitFullName[0]);
				user.setLastName(splitFullName[1]);
			}
			user.setPassword(encoder.encode(fullName));
			user = userRepository.save(user);
		}
		
		String token = Jwts.builder()
				.setSubject(user.getId().toString())
				.claim("ROLE", "USER")
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
				response.addHeader("auth", token);
				
	  response.sendRedirect(redirectURL);
		
	}

	private String getOAuth2TokenAttribute(OAuth2AuthenticationToken authToken,String attribute) {
		return authToken.getPrincipal().getAttribute(attribute);
	}

}
