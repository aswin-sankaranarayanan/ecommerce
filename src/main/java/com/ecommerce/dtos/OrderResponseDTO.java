package com.ecommerce.dtos;

import java.util.ArrayList;
import java.util.List;

public class OrderResponseDTO extends PagedResponseDTO<OrderDTO> {
	
	private List<OrderDTO> orders = new ArrayList<OrderDTO>();

	public List<OrderDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderDTO> orders) {
		this.orders = orders;
	}
	
	
	
}
