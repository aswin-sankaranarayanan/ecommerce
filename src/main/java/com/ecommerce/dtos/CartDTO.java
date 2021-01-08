package com.ecommerce.dtos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CartDTO extends BaseDTO {

	private List<CartItemDTO> cartItems;
	@JsonIgnore
	private UserResponseDTO user;
	private Date createdDate;

	public CartDTO() {
		this.createdDate = new Date();
		this.cartItems = new ArrayList<CartItemDTO>();
	}

	public List<CartItemDTO> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItemDTO> cartItems) {
		this.cartItems = cartItems;
	}

	public UserResponseDTO getUser() {
		return user;
	}

	public void setUser(UserResponseDTO user) {
		this.user = user;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Override
	public String toString() {
		return "CartDTO [cartItems=" + cartItems + ", user=" + user + ", createdDate=" + createdDate + ", id=" + id
				+ "]";
	}
}
