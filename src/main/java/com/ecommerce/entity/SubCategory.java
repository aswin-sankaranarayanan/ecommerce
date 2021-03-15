package com.ecommerce.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="SUB_CATEGORY")
public class SubCategory extends BaseEntity {

	private String subCategory;
	
	@ManyToOne
	@JoinColumn(name="CATEGORY_FK")
	private Category category;

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
}
