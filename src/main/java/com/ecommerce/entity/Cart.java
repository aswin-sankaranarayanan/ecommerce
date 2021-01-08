package com.ecommerce.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CART")
public class Cart extends BaseEntity{

	private Date createdDate;
	
	@OneToOne
	@JoinColumn(name = "USER_FK")
	private User user;
	
	@OneToMany(mappedBy = "cart",cascade = CascadeType.ALL)
	List<CartItem> cartItems = new ArrayList<CartItem>();
	
	public Cart() {
		this.createdDate = new Date();
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
		this.cartItems.forEach(cartItem->cartItem.setCart(this));
	}
	
}
