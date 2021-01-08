package com.ecommerce.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommerce.entity.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public UserDetailsImpl(User user) {
		super();
		System.out.println("User::"+user);
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+user.getRole());
		return List.of(authority);
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmailId();
	}

	@Override
	public boolean isAccountNonExpired() {
		return !user.getLocked();
	}

	@Override
	public boolean isAccountNonLocked() {
		return !user.getLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !user.getLocked();
	}

	@Override
	public boolean isEnabled() {
		return !user.getLocked();
	}

	public User getUser() {
		return user;
	}
}
