package com.ecommerce;

public class AppConstants {

	public static final String USER_ROLE="USER";
	public static final String USER_SIGNUP_SUCCESS="Successfully Registered";
	public static final String DELETE_USER_INFO_SUCCESS = "Address Deleted Successfully";
	public static final String USER_ALREADY_EXISTS = "User already exists";
	
	public static enum ORDER_STATUS {
		PENDING,
		PLACED,
		CANCELLED,
		DISPATCHED
	}

}
