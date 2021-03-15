package com.ecommerce.security;

import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.json.serializer.LoginResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = LoginResponseSerializer.class)
public class LoginResponseDTO {

	private UserResponseDTO user;
	private String token;
	private Long expiration;
	
	public UserResponseDTO getUser() {
		return user;
	}
	public void setUser(UserResponseDTO user) {
		this.user = user;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getExpiration() {
		return expiration;
	}
	public void setExpiration(Long expiration) {
		this.expiration = expiration;
	}
	
	@Override
	public String toString() {
		return "LoginResponseDTO [user=" + user + ", token=" + token + ", expiration=" + expiration + "]";
	}
	
	
	
}
