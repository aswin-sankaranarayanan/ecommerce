package com.ecommerce.dtos;

public class CartItemDTO extends BaseDTO {

	private InventoryDTO inventory;
	
	private int quantity;
	
	public CartItemDTO() {
		this.quantity = 1;
	}

	public InventoryDTO getInventory() {
		return inventory;
	}

	public void setInventory(InventoryDTO inventory) {
		this.inventory = inventory;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "CartItemDTO [inventory=" + inventory + ", quantity=" + quantity + ", id=" + id + "]";
	}

}
