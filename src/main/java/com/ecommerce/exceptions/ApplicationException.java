package com.ecommerce.exceptions;

import java.util.Date;

public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private Date timestamp;
	private String message;
	
	public ApplicationException(String message) {
		this.message = message;
		this.timestamp = new Date();
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
