package com.ecommerce.apis;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dtos.CartDTO;
import com.ecommerce.dtos.CartItemDTO;
import com.ecommerce.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartAPI {
	
	@Autowired
	private CartService cartService;
	
	@GetMapping
	public ResponseEntity<CartDTO> getCart(){
		CartDTO cartDTO = cartService.getCart();
		return ResponseEntity.ok(cartDTO);
	}

	@PostMapping
	public ResponseEntity<CartDTO> addItemToCart(@RequestBody @Valid CartDTO cartDTO){
		CartDTO savedCartDTO = cartService.addItemToCart(cartDTO);
		return ResponseEntity.ok(savedCartDTO);
	}
	
	@PutMapping
	public ResponseEntity<CartItemDTO> updateCartItemQuantity(@RequestBody @Valid CartItemDTO cartItemDTO){
		CartItemDTO savedCartItemDTO = cartService.updateCartItemQuantity(cartItemDTO);
		return ResponseEntity.ok(savedCartItemDTO);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCartItem(@NotNull @PathVariable("id") Long cartItemId){
		cartService.deleteCartItem(cartItemId);
		return ResponseEntity.ok().build();
	}
}
