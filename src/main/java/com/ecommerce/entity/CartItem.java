package com.ecommerce.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="CART_ITEMS")
public class CartItem extends BaseEntity{

	@OneToOne
	@JoinColumn(name="INVENTORY_FK")
	private Inventory inventory;
	
	@ManyToOne
	@JoinColumn(name = "CART_FK")
	private Cart cart;
	
	private int quantity;
	
	public CartItem() {
		this.quantity = 1;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}


	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}
	
}
