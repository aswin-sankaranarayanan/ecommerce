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
		User user = SessionUtils.getLoggedInUser();
		Cart userCart = getCartForUser();
		Cart savedCartEntity = null;
		if(userCart!=null) {
			for(CartItemDTO cartItemDTO: cartDTO.getCartItems()) {
				CartItem cartItem = convertToCartItemEntity(cartItemDTO);
				cartItem.setCart(userCart);
				userCart.getCartItems().add(cartItem);
			}
			savedCartEntity = cartReposiotry.save(userCart);
		}else {
			Cart cart = convertToEntity(cartDTO, Cart.class);
			cart.setUser(user);
			savedCartEntity = cartReposiotry.save(cart);
		}
		
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
	
	public void deleteCart(User user) {
		Cart cartFromDB = cartReposiotry.findByUser(user).orElse(null);
		cartReposiotry.delete(cartFromDB);
	}
	
	
	private Cart getCartForUser() {
		User user = SessionUtils.getLoggedInUser();
		Cart cart = cartReposiotry.findByUser(user).orElse(null);
		return cart;
	}
	
	public Integer getCartItemsCount() {
		Cart cart = getCartForUser();
		return cart!=null? cart.getCartItems().size():0;
	}
	
	
	 CartItem convertToCartItemEntity(CartItemDTO dto) {
		return (CartItem) mapper.map(dto, CartItem.class);
	}
}
