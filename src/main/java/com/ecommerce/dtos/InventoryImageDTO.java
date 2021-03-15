package com.ecommerce.dtos;

import java.util.Arrays;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class InventoryImageDTO extends BaseDTO {
	
	@NotBlank(message = "File Name is required")
	private String fileName;
	
	@NotNull(message= "Invalid Image")
	private  byte[] image;
	
	private String imagePath;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@JsonIgnore
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Override
	public String toString() {
		return "InventoryImageDTO [fileName=" + fileName + ", image=" + Arrays.toString(image) + ", id=" + id + "]";
	}
	
}
