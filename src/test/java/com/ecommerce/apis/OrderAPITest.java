package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MvcResult;
import com.ecommerce.AppConstants.ORDER_STATUS;
import com.ecommerce.dtos.OrderDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderAPITest extends BaseAPITest{
	
	@Autowired
	private ObjectMapper mapper;

	private static final String ENDPOINT = "/api/order";
	private static final String ADMIN_ENDPOINT = ENDPOINT+"/management";
	
	@Test
	@DisplayName("Place Order")
	@org.junit.jupiter.api.Order(1)
	void testPlaceOrder() {
		try {
			MvcResult mvcResult = mockMvc.perform(post(ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer "+getTokenForUser2()))
					.andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			OrderDTO actualOrderDTO = mapper.readValue(response.getContentAsByteArray(),OrderDTO.class);
			assertThat(actualOrderDTO.getTotalPrice()).isEqualTo(actualOrderDTO
													.getOrderDetails().stream().map(o->o.getPrice()*o.getQuantity()).reduce(0D, Double::sum));
			assertThat(actualOrderDTO.getStatus()).isEqualTo(ORDER_STATUS.PLACED.name());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Place Order");
		}
	}
	
	@Test
	@DisplayName("View Placed Order By User")
	@Order(2)
	void testViewPlacedOrdersByUser() {
		try {
			performGet(ENDPOINT);
			MvcResult mvcResult =  mockMvc.perform(get(ENDPOINT)
					.header("Authorization", "Bearer "+getTokenForUser2()))
					.andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			List<OrderDTO> ordersByUser = mapper.readValue(response.getContentAsByteArray(), new TypeReference<List<OrderDTO>>() {});
			assertThat(ordersByUser).hasSize(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("View Placed Order By User");
		}
	}
	
	@Test
	@DisplayName("View All Orders By Admin")
	@Order(3)
	void testViewAllOrdersByAdmin() {
		try {
			MockHttpServletResponse response = performGetByAdmin(ENDPOINT+"/management?status=ALL");
			assertThat(response.getStatus()).isEqualTo(200);
			List<OrderDTO> ordersByUser = mapper.readValue(response.getContentAsByteArray(), new TypeReference<List<OrderDTO>>() {});
			assertThat(ordersByUser).hasSize(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("View Placed Order By User");
		}
	}
	
	@Test
	@DisplayName("View All Orders By User")
	@Order(4)
	void testViewAllOrdersByUser() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT+"/management?status=ALL");
			assertThat(response.getStatus()).isEqualTo(403);
		} catch (Exception e) {
			e.printStackTrace();
			fail("View Placed Order By User");
		}
	}
	
	@Test
	@DisplayName("Update Order Status to Dispatched")
	@Order(5)
	public void testUpdateOrderStatusToDispatched() {
		var orderDTO = new OrderDTO();
		orderDTO.setId(1L);
		orderDTO.setStatus(ORDER_STATUS.DISPATCHED.name());
		try {
			MockHttpServletResponse response = performPutByAdmin(ADMIN_ENDPOINT, orderDTO);
			assertThat(response.getStatus()).isEqualTo(200);
			OrderDTO savedOrderDTO = mapper.readValue(response.getContentAsByteArray(), OrderDTO.class);
			assertThat(savedOrderDTO.getId()).isEqualTo(orderDTO.getId());
			assertThat(savedOrderDTO.getStatus()).isEqualTo(ORDER_STATUS.DISPATCHED.name());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Order Status to Dispatched");
		}
	}
	
	@Test
	@DisplayName("Cancel Order By User")
	@Order(6)
	void testCancelOrderByUser() {
		var orderDTO = new OrderDTO();
		orderDTO.setId(1L);
		orderDTO.setStatus(ORDER_STATUS.CANCELLED.name());
		try {
			MvcResult mvcResult =  mockMvc.perform(put(ENDPOINT)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(orderDTO))
					.header("Authorization", "Bearer "+getTokenForUser2()))
					.andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			OrderDTO savedOrderDTO = mapper.readValue(response.getContentAsByteArray(),OrderDTO.class);
			assertThat(savedOrderDTO.getId()).isEqualTo(1L);
			assertThat(savedOrderDTO.getStatus()).isEqualTo(ORDER_STATUS.CANCELLED.name());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Cancel Order By User");
		}
	}
	
	@Test
	@DisplayName("View Cancelled Orders By Admin")
	@Order(7)
	void testViewAllCancelledOrdersByUser() {
		try {
			MockHttpServletResponse response = performGetByAdmin(ENDPOINT+"/management?status="+ORDER_STATUS.CANCELLED);
			assertThat(response.getStatus()).isEqualTo(200);
			List<OrderDTO> ordersByUser = mapper.readValue(response.getContentAsByteArray(), new TypeReference<List<OrderDTO>>() {});
			assertThat(ordersByUser).hasSize(1);
		} catch (Exception e) {
			e.printStackTrace();
			fail("View Cancelled Orders By Admin");
		}
	}
	
	
	
}
