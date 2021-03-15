package com.ecommerce.dtos;

import java.util.List;

import javax.validation.constraints.NotBlank;

public class CategoryDTO extends BaseDTO {
	
	private String categoryName;
	
	private List<SubCategoryDTO> subCategories;
	
	public CategoryDTO() {}
	
	public CategoryDTO(Long id) {
		super(id);
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<SubCategoryDTO> getSubCategories() {
		return subCategories;
	}
	public void setSubCategories(List<SubCategoryDTO> subCategories) {
		this.subCategories = subCategories;
	}
	
	
}
