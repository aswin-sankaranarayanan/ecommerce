package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import com.ecommerce.AppConstants;
import com.ecommerce.dtos.UserInfoDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserInfoAPITest extends BaseAPITest {
	
	private static final String ENDPOINT="/api/user-info";
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;


	@Test
	@DisplayName("Save User Information")
	@Order(1)
	public void testSaveUserInfo() {
		
		UserInfoDTO userInfo = new UserInfoDTO();
		userInfo.setCity("Chennai");
		userInfo.setState("Tamil Nadu");
		userInfo.setAddressLine1("4/708 Second Street");
		userInfo.setAddressLine2("Captain Sasi Kumar Nagar, New Perungalathur");
		userInfo.setZipcode(600063);
		
		try {
		MockHttpServletResponse response = performPost(ENDPOINT, userInfo);
		assertThat(response.getStatus()).isEqualTo(200);
		
		UserInfoDTO actualUserInfo = mapper.readValue(response.getContentAsString().getBytes(), UserInfoDTO.class);
		assertThat(actualUserInfo.getCity()).isEqualTo(userInfo.getCity());
		assertThat(actualUserInfo.getAddressLine1()).isEqualTo(userInfo.getAddressLine1());
		assertThat(actualUserInfo.getAddressLine2()).isEqualTo(userInfo.getAddressLine2());
		assertThat(actualUserInfo.getZipcode()).isEqualTo(userInfo.getZipcode());
		
		}catch (Exception e) {
			e.printStackTrace();
			fail("Save User Information");
		}
	}
	
	@Test
	@DisplayName("Get User Info")
	@Order(2)
	public void testGetUserInfo() {
		try {
			MockHttpServletResponse response =performGet(ENDPOINT+"/1");
			assertThat(response.getStatus()).isEqualTo(200);
			List<UserInfoDTO> actualUserInfos = mapper.readValue(response.getContentAsString().getBytes(), new TypeReference<List<UserInfoDTO>>(){});
			assertThat(actualUserInfos).hasSizeGreaterThan(0);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Get User Information");
		}
	}
	
	@Test
	@DisplayName("Update User Info")
	@Order(3)
	public void testUpdateUserInfo() {
		try {
			UserInfoDTO updatedUserInfoDTO = new UserInfoDTO();
			updatedUserInfoDTO.setId(1L);
			updatedUserInfoDTO.setCity("Chennai");
			updatedUserInfoDTO.setState("Tamil Nadu");
			updatedUserInfoDTO.setAddressLine1("4/708 Third Street");
			updatedUserInfoDTO.setAddressLine2("Captain Sasi Kumar Nagar, Old Perungalathur");
			updatedUserInfoDTO.setZipcode(600045);
			
			MockHttpServletResponse response = performPut(ENDPOINT, updatedUserInfoDTO);
			assertThat(response.getStatus()).isEqualTo(200);
			UserInfoDTO actualUserInfoDTO = mapper.readValue(response.getContentAsString().getBytes(), UserInfoDTO.class);
			assertThat(updatedUserInfoDTO).isEqualTo(actualUserInfoDTO);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Update User Info");
		}
	}
	
	@Test
	@DisplayName("Delete User Info")
	@Order(4)
	public void testDeleteUserInfo() {
		try {
			MockHttpServletResponse response = performDelete(ENDPOINT+"/1");
			assertThat(response.getStatus()).isEqualTo(200);
			assertThat(response.getContentAsString()).isEqualTo(AppConstants.DELETE_USER_INFO_SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Get User Information");
		}
	}
}
