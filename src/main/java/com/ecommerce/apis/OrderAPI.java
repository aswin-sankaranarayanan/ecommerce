package com.ecommerce.apis;

import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.dtos.OrderResponseDTO;
import com.ecommerce.dtos.RazorPayResponse;
import com.ecommerce.dtos.UserInfoDTO;
import com.ecommerce.entity.OrderTransactionDetails;
import com.ecommerce.repository.OrderProjection;
import com.ecommerce.services.OrderService;
import com.razorpay.RazorpayException;

@RestController
@RequestMapping("/api/order")
public class OrderAPI {

	@Autowired
	private OrderService orderService;

	@PostMapping
	public ResponseEntity<RazorPayResponse> placeOrder(@RequestBody UserInfoDTO userInfo) throws RazorpayException {
		RazorPayResponse order= orderService.placeRazorPayOrder(userInfo);
		return ResponseEntity.ok(order);
	}
	
	/*
	 Paytm Work Flow
	@PostMapping
	public ResponseEntity<OrderDTO> placeOrder(@RequestBody UserInfoDTO userInfo) {
		OrderDTO orderDTO = orderService.placeOrder(userInfo);
		return ResponseEntity.ok(orderDTO);
	}
	*/

	@GetMapping
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<List<OrderProjection>> getPlacedOrdersByUser() {
		List<OrderProjection> orders = orderService.getPlacedOrdersByUser();
		return ResponseEntity.ok(orders);
	}

	@PutMapping
	public ResponseEntity<OrderDTO> updateOrder(@RequestBody OrderDTO orderDTO) {
		OrderDTO updatedOrderDTO = orderService.updateOrder(orderDTO);
		return ResponseEntity.ok(updatedOrderDTO);
	}

	@GetMapping("/management")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<OrderResponseDTO> getOrders(
			@NotBlank(message = "Status is required") @RequestParam(name="status",defaultValue = "ALL") String status,
			@RequestParam(name = "size",defaultValue = "3") Integer size,
			@RequestParam(name = "page",defaultValue = "0") Integer pageNumber) {
		OrderResponseDTO orders = orderService.getAllOrders(status,pageNumber,size);
		return ResponseEntity.ok(orders);
	}
	
	@GetMapping("/management/{id}")
	public ResponseEntity<List<OrderProjection>> getOrderDetails(@PathVariable("id") Long id) {
		List<OrderProjection> orderDetails = orderService.getOrderDetails(id);
		return ResponseEntity.ok(orderDetails);
	}
	
	@GetMapping("/management/tx/{id}")
	public ResponseEntity<OrderTransactionDetails> getTransactionDetails(@PathVariable("id") Long id) {
		OrderTransactionDetails txDetails = orderService.getTransactionDetails(id);
		return ResponseEntity.ok(txDetails);
	}
	
	@PutMapping("/management")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<OrderDTO> updateOrderByAdmin(@RequestBody OrderDTO orderDTO) {
		OrderDTO updatedOrderDTO = orderService.updateOrder(orderDTO);
		return ResponseEntity.ok(updatedOrderDTO);
	}
}
