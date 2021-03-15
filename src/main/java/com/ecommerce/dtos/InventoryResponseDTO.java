package com.ecommerce.dtos;

import java.util.ArrayList;
import java.util.List;

import com.ecommerce.json.serializer.InventoryResponseSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class InventoryResponseDTO extends PagedResponseDTO<InventoryDTO> {

	@JsonSerialize(using=InventoryResponseSerializer.class)
	private List<InventoryDTO> inventory = new ArrayList<InventoryDTO>();
	
	public List<InventoryDTO> getInventory() {
		return inventory;
	}
	public void setInventory(List<InventoryDTO> inventory) {
		this.inventory = inventory;
	}
	
}
