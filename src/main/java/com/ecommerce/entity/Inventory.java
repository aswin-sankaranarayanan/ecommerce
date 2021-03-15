package com.ecommerce.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="INVENTORY")
public class Inventory extends BaseEntity {

	private String item;
	@Lob
	private String description;
	private boolean available;
	private Double price;
	private String language;
	
	@OneToOne
	@JoinColumn(name = "CATEGORY_FK")
	private Category category;
	
	@OneToOne
	@JoinColumn(name = "SUB_CATEGORY_FK")
	private SubCategory subCategory;
	
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

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}

}
