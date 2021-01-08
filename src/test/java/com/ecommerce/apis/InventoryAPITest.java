package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ecommerce.dtos.InventoryDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryAPITest extends BaseAPITest {

	private static final String ENDPOINT = "/api/inventory";

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper mapper;

	@Test
	@DisplayName("Add Item to Inventory By ROLE_ADMIN")
	@Order(1)
	void testAdminAddItemToInventory() {
		var givenItem = new InventoryDTO();
		givenItem.setAvailable(true);
		givenItem.setItem("Dummy");
		givenItem.setDescription("Dummy Description");
		givenItem.setPrice(25.00);
		givenItem.setCategory("Dummy Category");
		try {
			MvcResult mvcResult = mockMVC
					.perform(post(ENDPOINT).header("Authorization", "Bearer " + getTokenForAdmin())
							.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(givenItem)))
					.andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryDTO responseDTO = mapper.readValue(response.getContentAsString().getBytes(), InventoryDTO.class);
			assertThat(responseDTO).isNotNull();
			assertThat(responseDTO.getId()).isGreaterThan(0);
			assertThat(responseDTO.getItem()).isEqualTo(givenItem.getItem());
			assertThat(responseDTO.getDescription()).isEqualTo(givenItem.getDescription());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Add Item to Inventory By ROLE_ADMIN");
		}
	}

	@Test
	@DisplayName("Upload Images for Inventory Item")
	@Order(2)
	void testUploadImages() {
		try {
			ClassPathResource classPathResource1 = new ClassPathResource("/test-images/udayar-1.jpg");
			ClassPathResource classPathResource2 = new ClassPathResource("/test-images/udayar-2.jpg");
			ClassPathResource classPathResource3 = new ClassPathResource("/test-images/udayar-3.jpg");

			MockMultipartFile file1 = new MockMultipartFile("inventoryImages", "udayar-1.jpg", "image/jpeg",
									  classPathResource1.getInputStream());

			MockMultipartFile file2 = new MockMultipartFile("inventoryImages", "udayar-2.jpg", "image/jpeg",
									  classPathResource2.getInputStream());

			MockMultipartFile file3 = new MockMultipartFile("inventoryImages", "udayar-3.jpg", "image/jpeg",
									  classPathResource3.getInputStream());
			
			
			MvcResult mvcResult = mockMvc.perform(
					multipart(ENDPOINT + "/image")
					.file(file1).file(file2).file(file3)
					.param("id", "1")
					.header("Authorization", "Bearer " + getTokenForAdmin())).andReturn();
			
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryDTO savedInventoryDTO = mapper.readValue(response.getContentAsByteArray(), InventoryDTO.class);
			assertThat(savedInventoryDTO.getInventoryImages()).hasSize(3);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Failed to upload Images");
		}

	}

	@Test
	@DisplayName("Get All Inventory Items")
	@Order(3)
	void testGetInventory() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT);
			assertThat(response.getStatus()).isEqualTo(200);
			Iterable<InventoryDTO> inventoryDTOs = mapper.readValue(response.getContentAsString().getBytes(),
					new TypeReference<Iterable<InventoryDTO>>() {
					});
			assertThat(inventoryDTOs).hasSizeGreaterThan(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Get Inventory Item By Id")
	@Order(4)
	void testGetInventoryItem() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT + "/1");
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryDTO responseDTO = mapper.readValue(response.getContentAsString().getBytes(), InventoryDTO.class);
			assertThat(responseDTO).isNotNull();
			assertThat(responseDTO.getId()).isEqualTo(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Add Item to Inventory By ROLE_USER")
	void testAddItemToInventory() {
		InventoryDTO item = new InventoryDTO();
		item.setAvailable(true);
		item.setItem("Dummy");
		item.setDescription("Dummy Description");
		item.setPrice(25.00);
		item.setCategory("Dummy Category");
		try {
			MockHttpServletResponse response = performPost(ENDPOINT, item);
			assertThat(response.getStatus()).isEqualTo(403);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Update Item By Admin")
	void testAdminUpdateItem() {
		var givenItem = new InventoryDTO();
		givenItem.setId(1L);
		givenItem.setAvailable(true);
		givenItem.setItem("Dummy");
		givenItem.setDescription("Update Item Description");
		givenItem.setPrice(50.00);
		givenItem.setCategory("Dummy Category");
		try {
			MvcResult mvcResult = mockMVC.perform(put(ENDPOINT).contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + getTokenForAdmin())
					.content(mapper.writeValueAsString(givenItem))).andReturn();
			MockHttpServletResponse mockResponse = mvcResult.getResponse();
			assertThat(mockResponse.getStatus()).isEqualTo(200);
			var actualInventoryDTO = mapper.readValue(mockResponse.getContentAsString().getBytes(), InventoryDTO.class);
			assertThat(actualInventoryDTO.getId()).isEqualTo(givenItem.getId());
			assertThat(actualInventoryDTO.getPrice()).isEqualTo(givenItem.getPrice());
			assertThat(actualInventoryDTO.getItem()).isEqualTo(givenItem.getItem());
			assertThat(actualInventoryDTO.getDescription()).isEqualTo(givenItem.getDescription());

		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Item By Admin");
		}
	}

	@Test
	@DisplayName("Update Item By User")
	void testUserUpdateItem() {
		var givenItem = new InventoryDTO();
		givenItem.setId(1L);
		givenItem.setAvailable(true);
		givenItem.setItem("Dummy");
		givenItem.setDescription("Update Item Description");
		givenItem.setPrice(50.00);
		givenItem.setCategory("Dummy Category");

		try {
			MockHttpServletResponse mockResponse = performPut(ENDPOINT, givenItem);
			assertThat(mockResponse.getStatus()).isEqualTo(403);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Item By Admin");
		}
	}

	@Test
	@DisplayName("Soft Delete Item By Admin")
	void testAdminSoftDeleteItem() {
		try {
			MvcResult mvcResult = mockMVC
					.perform(delete(ENDPOINT + "/1").header("Authorization", "Bearer " + getTokenForAdmin()))
					.andReturn();
			MockHttpServletResponse mockResponse = mvcResult.getResponse();
			assertThat(mockResponse.getStatus()).isEqualTo(200);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Item By Admin");
		}
	}

	@Test
	@DisplayName("Soft Delete Item By User")
	void testUserSoftDeleteItem() {
		try {
			MockHttpServletResponse mockResponse = performDelete(ENDPOINT + "/1");
			assertThat(mockResponse.getStatus()).isEqualTo(403);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Item By Admin");
		}
	}
	
	@Test
	@DisplayName("Delete Inventory Image By Admin")
	void testDeleteInventoryImageByAdmin() {
		try {
			MvcResult mvcResult = mockMVC.perform(delete(ENDPOINT+"/image/1").contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + getTokenForAdmin())).andReturn();
			assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Delete Inventory Image By Admin");
		}
	}

}
