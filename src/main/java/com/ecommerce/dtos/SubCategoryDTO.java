package com.ecommerce.dtos;

import javax.validation.constraints.NotBlank;

public class SubCategoryDTO extends BaseDTO {

	private String subCategory;
	
	
	public SubCategoryDTO() {}
	
	public SubCategoryDTO(Long id) {
		super(id);
	}
	
	public SubCategoryDTO(Long id, String subCategory) {
		super(id);
		this.subCategory = subCategory;
	}
	

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	
}
