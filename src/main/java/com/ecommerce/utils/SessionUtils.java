package com.ecommerce.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.ecommerce.entity.User;

@Service
public class SessionUtils {

	public static User getLoggedInUser() {
		User user = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof String) {
			Long id = Long.valueOf((String) principal);
			user = new User();
			user.setId(id);
		}
		return user;
	}
}
