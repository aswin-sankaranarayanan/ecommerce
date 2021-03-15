package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryResponseDTO;
import com.ecommerce.dtos.SubCategoryDTO;
import com.ecommerce.repository.InventoryProjection;
import com.ecommerce.services.AWSUtilityService;
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

	private CategoryDTO category = new CategoryDTO(1L);
	private SubCategoryDTO subCategory = new SubCategoryDTO(1L);

	@TestConfiguration
	public static class TestConfig {

		@Bean
		@Primary
		public AWSUtilityService aWSUtilityService() throws Exception {
			AWSUtilityService service = Mockito.mock(AWSUtilityService.class);
			Mockito.doNothing().when(service).setupS3Client();
			Mockito.when(service.uploadFilesToS3(Mockito.any())).thenReturn("URL");
			return service;
		}
	}

	@Test
	@DisplayName("Save Inventory Item")
	@Order(1)
	void testSaveInventory() throws Exception {

		try {
			ClassPathResource classPathResource1 = new ClassPathResource("/test-images/udayar-1.jpg");
			ClassPathResource classPathResource2 = new ClassPathResource("/test-images/udayar-2.jpg");
			ClassPathResource classPathResource3 = new ClassPathResource("/test-images/udayar-3.jpg");

			MockMultipartFile file1 = new MockMultipartFile("images", "udayar-1.jpg", "image/jpeg",
					classPathResource1.getInputStream());

			MockMultipartFile file2 = new MockMultipartFile("images", "udayar-2.jpg", "image/jpeg",
					classPathResource2.getInputStream());

			MockMultipartFile file3 = new MockMultipartFile("images", "udayar-3.jpg", "image/jpeg",
					classPathResource3.getInputStream());

			MvcResult mvcResult = mockMvc.perform(multipart(ENDPOINT).file(file1).file(file2).file(file3)
					.param("item", "Test Book1").param("description", "Test Description").param("category", "1")
					.param("subCategory", "1").param("available", "true").param("price", "1500")
					.param("language", "Tamil").header("Authorization", "Bearer " + getTokenForAdmin())).andReturn();

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
	@Order(2)
	void testGetInventory() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT + "?page=0&size=10");
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryResponseDTO inventoryResponseDTO = mapper.readValue(response.getContentAsString().getBytes(),
					InventoryResponseDTO.class);
			assertThat(inventoryResponseDTO.getInventory()).hasSizeGreaterThan(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	@DisplayName("Filter Inventory")
	void testFilterInventory() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT + "?page=0&size=10&filter=Thi");
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryResponseDTO inventoryResponseDTO = mapper.readValue(response.getContentAsString().getBytes(),
					InventoryResponseDTO.class);
			assertThat(inventoryResponseDTO.getInventory()).hasSizeGreaterThan(0);
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
		try {
			ClassPathResource classPathResource1 = new ClassPathResource("/test-images/udayar-1.jpg");
			ClassPathResource classPathResource2 = new ClassPathResource("/test-images/udayar-2.jpg");
			ClassPathResource classPathResource3 = new ClassPathResource("/test-images/udayar-3.jpg");

			MockMultipartFile file1 = new MockMultipartFile("images", "udayar-1.jpg", "image/jpeg",
					classPathResource1.getInputStream());

			MockMultipartFile file2 = new MockMultipartFile("images", "udayar-2.jpg", "image/jpeg",
					classPathResource2.getInputStream());

			MockMultipartFile file3 = new MockMultipartFile("images", "udayar-3.jpg", "image/jpeg",
					classPathResource3.getInputStream());

			MvcResult mvcResult = mockMvc
					.perform(multipart(ENDPOINT).file(file1).file(file2).file(file3).param("item", "Test Book1")
							.param("description", "Test Description").param("category", "1").param("subCategory", "1")
							.param("available", "true").param("price", "1500").param("language", "Tamil")

							.header("Authorization", "Bearer ".concat(generateTokenForUser())))
					.andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
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
		givenItem.setCategory(category);
		givenItem.setSubCategory(subCategory);
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
		givenItem.setCategory(category);
		givenItem.setSubCategory(subCategory);

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
			MvcResult mvcResult = mockMVC.perform(delete(ENDPOINT + "/image/1").contentType(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + getTokenForAdmin())).andReturn();
			assertThat(mvcResult.getResponse().getStatus()).isEqualTo(200);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Delete Inventory Image By Admin");
		}
	}

	@Test
	@DisplayName("Update Inventory Image")
	void testUpdateInventoryImage() {
		try {
			ClassPathResource resource = new ClassPathResource("test-images/udayar-4.jpg");
			MockMultipartFile file1 = new MockMultipartFile("images", "udayar-4.jpg", "image/jpeg",
					resource.getInputStream());
			MvcResult mvcResult = mockMvc.perform(multipart(ENDPOINT + "/image").file(file1).param("id", "1")
					.header("Authorization", "Bearer " + getTokenForAdmin())).andReturn();
			MockHttpServletResponse response = mvcResult.getResponse();
			assertThat(response.getStatus()).isEqualTo(200);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Inventory Image");
		}
	}

	@Test
	@Disabled
	@DisplayName("Get Items By Category")
	void testGetItemsByCategory() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT + "/list/category/1");
			assertThat(response.getStatus()).isEqualTo(200);
			Map<String,List<InventoryProjection>> map = mapper.readValue(response.getContentAsByteArray(), Map.class);
			assertThat(map).isNotEmpty();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update Inventory Image");
		}
	}
	
	@Test
	@DisplayName("Get Item By Name")
	void testGetItemByName() {
		try {
			MockHttpServletResponse response = performGet(ENDPOINT+"/view?item=Udayar");
			assertThat(response.getStatus()).isEqualTo(200);
			InventoryDTO expectedItem = mapper.readValue(response.getContentAsByteArray(), InventoryDTO.class);
			assertThat(expectedItem.getItem()).isEqualTo("Udayar");
		} catch (Exception e) {
			e.printStackTrace();
			fail("Get Item By Name");
		}
	}
}