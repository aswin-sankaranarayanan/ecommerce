package com.ecommerce.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="INVENTORY")
public class Inventory extends BaseEntity {

	private String item;
	@Lob
	private String description;
	private String category;
	private boolean available;
	private Double price;
	
	@OneToMany(mappedBy = "inventory",cascade = CascadeType.ALL)
	private List<InventoryImage> inventoryImages;
	
	public Inventory() {
		this.available = true;
		this.inventoryImages = new ArrayList<InventoryImage>();
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public List<InventoryImage> getInventoryImages() {
		return inventoryImages;
	}

	public void setInventoryImages(List<InventoryImage> inventoryImages) {
		this.inventoryImages = inventoryImages;
		this.inventoryImages.forEach(image-> image.setInventory(this));
	}
	
	public void addInventoryImage(InventoryImage inventoryImage) {
		this.inventoryImages.add(inventoryImage);
		inventoryImage.setInventory(this);
	}
	
	
}
