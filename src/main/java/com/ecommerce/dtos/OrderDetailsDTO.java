package com.ecommerce.dtos;

public class OrderDetailsDTO extends BaseDTO {

	private String item;
	private int quantity;
	private double price;

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderDetailsDTO [item=" + item + ", quantity=" + quantity + ", price=" + price + "]";
	}
}
