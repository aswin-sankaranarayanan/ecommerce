package com.ecommerce.dtos;

public class ResponseDTO {

	private String message;
	private Object response;
	
	public ResponseDTO(String message, Object response) {
		super();
		this.message = message;
		this.response = response;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}
	
}
