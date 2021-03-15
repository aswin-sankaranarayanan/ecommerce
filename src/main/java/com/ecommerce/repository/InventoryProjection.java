package com.ecommerce.repository;

public interface InventoryProjection {
	public Long getId();
	public String getItem();
	public Double getPrice();
	public String getSubCategory();
	public String getImagePath();
	public String getDescription();
	
}
