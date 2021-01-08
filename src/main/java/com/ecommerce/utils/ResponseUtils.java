package com.ecommerce.utils;

import org.springframework.http.ResponseEntity;

public class ResponseUtils {

	public static ResponseEntity<String> getResponseEntity(String message) {
		return ResponseEntity.ok(message);
	}

}
