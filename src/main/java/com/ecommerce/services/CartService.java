package com.ecommerce.services;

import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.dtos.CartDTO;
import com.ecommerce.dtos.CartItemDTO;
import com.ecommerce.entity.Cart;
import com.ecommerce.entity.CartItem;
import com.ecommerce.entity.User;
import com.ecommerce.exceptions.ApplicationException;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.utils.SessionUtils;

@Service
public class CartService extends BaseService<Cart, CartDTO> {
	
	@Autowired
	private CartRepository cartReposiotry;
	
	@Autowired
	private CartItemRepository cartItemReposiotry;
	
	public CartDTO addItemToCart(CartDTO cartDTO) {
		Cart cart = convertToEntity(cartDTO, Cart.class);
		User user = SessionUtils.getLoggedInUser();
		cart.setUser(user);
		Cart savedCartEntity = cartReposiotry.save(cart);
		return convertToDTO(savedCartEntity, CartDTO.class);
	}

	public CartDTO getCart() {
		Cart cart = getCartForUser();
		return convertToDTO(cart,CartDTO.class);
	}

	

	public CartItemDTO updateCartItemQuantity(CartItemDTO cartItemDTO) {
		System.out.println("Cart Item Id:"+cartItemDTO.getId());
		CartItem cartItemFromDB =cartItemReposiotry.findById(cartItemDTO.getId())
							.orElseThrow(()->  new RuntimeException("Invalid Cart Item"));
		BeanUtils.copyProperties(cartItemDTO, cartItemFromDB);
		CartItem savedCartItemEntity = cartItemReposiotry.save(cartItemFromDB);
		BeanUtils.copyProperties(savedCartItemEntity, cartItemDTO);
		return cartItemDTO;
	}


	public void deleteCartItem(@NotNull Long cartItemId) {
		cartItemReposiotry.deleteById(cartItemId);
	}
	
	public void deleteCart() {
		Cart cartFromDB = getCartForUser();
		cartReposiotry.delete(cartFromDB);
	}
	
	
	private Cart getCartForUser() {
		User user = SessionUtils.getLoggedInUser();
		System.out.println("Get Cart For User"+user.getId());
		Cart cart = cartReposiotry.findByUser(user).orElseThrow(()-> new ApplicationException("Empty Cart!"));
		return cart;
	}
}
