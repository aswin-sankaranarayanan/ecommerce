package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import com.ecommerce.dtos.CartDTO;
import com.ecommerce.dtos.CartItemDTO;
import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.SubCategoryDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CartAPITest extends BaseAPITest {

	private static final String ENDPOINT = "/api/cart";

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("Add items to cart")
	@Order(1)
	void testAddItemsToCart() {
		List<CartItemDTO> cartItems = new ArrayList<CartItemDTO>();
		CartDTO cartDTO = new CartDTO();
		for (int i = 1; i <= 3; i++) {
			InventoryDTO inventoryDTO = new InventoryDTO();
			inventoryDTO.setId(Long.valueOf(i));
			inventoryDTO.setItem("Dummy");
			inventoryDTO.setDescription("Dummy");
			inventoryDTO.setPrice(25.00);
			inventoryDTO.setCategory(new CategoryDTO(1L));
			inventoryDTO.setSubCategory(new SubCategoryDTO(1L));
			inventoryDTO.setInventoryImages(Collections.EMPTY_LIST);
			
			CartItemDTO cartItem = new CartItemDTO();
			cartItem.setInventory(inventoryDTO);
			cartItem.setQuantity(1);
			cartItems.add(cartItem);
		}
		cartDTO.setCartItems(cartItems);
		try {
			MockHttpServletResponse response = performPost(ENDPOINT, cartDTO);
			assertThat(response.getStatus()).isEqualTo(200);
			CartDTO actualCartDTO = mapper.readValue(response.getContentAsString().getBytes(), CartDTO.class);
			assertThat(actualCartDTO.getId()).isGreaterThan(0L);
			assertThat(actualCartDTO.getCartItems()).hasSize(3);
			actualCartDTO.getCartItems().forEach(cartItem -> assertTrue(cartItem.getInventory().getId() > 0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Add items to cart");
		}
	}

	@Test
	@DisplayName("View Cart")
	@Order(2)
	public void testViewCart() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT);
			assertThat(response.getStatus()).isEqualTo(200);
			CartDTO actualCartDTO = mapper.readValue(response.getContentAsString().getBytes(), CartDTO.class);
			assertThat(actualCartDTO.getCartItems().size()).isEqualTo(3);
		} catch (Exception e) {
			e.printStackTrace();
			fail("View Cart");
		}
	}

	@Test
	@DisplayName("Update Item Quantity")
	@Order(3)
	public void updateQuantity() {
		InventoryDTO inventoryDTO = new InventoryDTO();
		inventoryDTO.setId(1L);
		inventoryDTO.setItem("Dummy");
		inventoryDTO.setDescription("Dummy");
		inventoryDTO.setPrice(25.00);
		inventoryDTO.setCategory(new CategoryDTO(1L));
		inventoryDTO.setSubCategory(new SubCategoryDTO(1L));
		inventoryDTO.setInventoryImages(Collections.EMPTY_LIST);
		
		CartItemDTO cartItem = new CartItemDTO();
		cartItem.setId(1003L);
		cartItem.setInventory(inventoryDTO);
		cartItem.setQuantity(2);
		try {
			MockHttpServletResponse response = performPut(ENDPOINT, cartItem);
			assertThat(response.getStatus()).isEqualTo(200);
			CartItemDTO actualCartDTO = mapper.readValue(response.getContentAsByteArray(), CartItemDTO.class);
			assertThat(actualCartDTO.getQuantity()).isEqualTo(cartItem.getQuantity());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Item Quantity");
		}
	}

	@Test
	@DisplayName("Delete Cart Item")
	@Order(4)
	public void testDeleteCartItem() {
		try {
			MockHttpServletResponse response = performDelete(ENDPOINT + "/1003");
			assertThat(response.getStatus()).isEqualTo(200);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Delete Cart Item");
		}
	}

}
