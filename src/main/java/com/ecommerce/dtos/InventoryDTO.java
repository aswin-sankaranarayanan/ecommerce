package com.ecommerce.dtos;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import com.ecommerce.json.serializer.InventorySerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using= InventorySerializer.class)
public class InventoryDTO extends BaseDTO {

	@NotBlank(message = "Item Name is required")
	private String item;
	
	@NotBlank(message = "Item Description is required")
	private String description;
	
	
	private CategoryDTO category;
	
	private SubCategoryDTO subCategory;
	
	private String language;
	
	private boolean available;
	
	@NotNull(message = "Item Price is required")
	@Min(value = 0, message = "Item Price must be positive")
	private Double price;
	
	private List<InventoryImageDTO> inventoryImages;
	
	public InventoryDTO() {
		this.available = true;
		this.inventoryImages = new ArrayList<InventoryImageDTO>();
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
	
	public List<InventoryImageDTO> getInventoryImages() {
		return inventoryImages;
	}

	public void setInventoryImages(List<InventoryImageDTO> inventoryImages) {
		this.inventoryImages = inventoryImages;
	}
	
	public void addInventoryImage(InventoryImageDTO inventoryImage) {
		this.inventoryImages.add(inventoryImage);
	}
	
	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory(CategoryDTO category) {
		this.category = category;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	

	public SubCategoryDTO getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategoryDTO subCategory) {
		this.subCategory = subCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (available ? 1231 : 1237);
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventoryDTO other = (InventoryDTO) obj;
		if (available != other.available)
			return false;
		if (category == null) {
			if (other.category != null)
				return false;
		} else if (!category.equals(other.category))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "InventoryDTO [item=" + item + ", description=" + description + ", category=" + category + ", available="
				+ available + ", price=" + price + ", id=" + id + "]";
	}
}
