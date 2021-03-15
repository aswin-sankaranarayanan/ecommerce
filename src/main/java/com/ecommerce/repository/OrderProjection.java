package com.ecommerce.repository;

public interface OrderProjection {

	public Long getId();
	public String getCreatedDate();
	public String getItem();
	public Double getPrice();
	public String getImagePath();
	
}
