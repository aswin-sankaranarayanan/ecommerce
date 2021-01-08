package com.ecommerce.apis;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.services.OrderService;

@RestController
@RequestMapping("/api/order")
public class OrderAPI {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<OrderDTO> placeOrder() {
		OrderDTO orderDTO = orderService.placeOrder();
		return ResponseEntity.ok(orderDTO);
	}

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<OrderDTO>> getPlacedOrdersByUser() {
		List<OrderDTO> orders = orderService.getPlacedOrdersByUser();
		return ResponseEntity.ok(orders);
	}

	@PutMapping
	public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO) {
		OrderDTO updatedOrderDTO = orderService.updateOrder(orderDTO);
		return ResponseEntity.ok(updatedOrderDTO);
	}

	@GetMapping("/management")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<List<OrderDTO>> getAllPlacedOrders(
			@NotBlank(message = "Status is required") @RequestParam("status") String status) {
		System.out.println("Status:" + status);
		List<OrderDTO> orders = orderService.getAllOrders(status);
		return ResponseEntity.ok(orders);
	}
	
	@PutMapping("/management")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<OrderDTO> updateOrderByAdmin(@RequestBody OrderDTO orderDTO) {
		OrderDTO updatedOrderDTO = orderService.updateOrder(orderDTO);
		return ResponseEntity.ok(updatedOrderDTO);
	}
}
