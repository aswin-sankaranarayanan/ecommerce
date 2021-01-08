package com.ecommerce.dtos;

import static com.ecommerce.AppConstants.USER_ROLE;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;;

public class UserRequestDTO {


	@NotBlank(message = "First Name is Required")
	private String firstName;

	@NotBlank(message = "Last Name is Required")
	private String lastName;

	@NotBlank(message = "Email is Required")
	@Email(message = "Not a Valid EMail")
	private String emailId;

	@Size(min = 8, max = 16, message = "Password must be 8 to 16 characters")
	private String password;

	private String role;
	private boolean locked;

	public UserRequestDTO() {
		this.role = USER_ROLE;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public boolean getLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public String toString() {
		return "UserRequestDTO [firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", password=" + password + ", role=" + role + ", locked=" + locked + "]";
	}

}
