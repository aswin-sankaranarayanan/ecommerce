package com.ecommerce.apis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.ecommerce.AppConstants;
import com.ecommerce.dtos.ResponseDTO;
import com.ecommerce.dtos.UserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserManagementAPITest {

	@Autowired
	private MockMvc mockMVC;

	@Autowired
	private ObjectMapper mapper;

	private MvcResult sendRequest(UserRequestDTO userDTO) {
		MvcResult mvcResult = null;
		try {
			mvcResult = mockMVC.perform(post("/api/user-management/signup").contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(userDTO))).andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mvcResult;
	}

	@Test
	@DisplayName("User Sign Up")
	@Order(1)
	void testSignup()  {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("Test@123");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(200);
		try {
			ResponseDTO responseDTO = mapper.readValue(response.getContentAsString().getBytes(),ResponseDTO.class);
			assertThat(responseDTO.getMessage()).isEqualTo(AppConstants.USER_SIGNUP_SUCCESS);

		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Signup for Existing User")
	void testExistingUser() throws Exception{
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("Test@123");
		
		MvcResult mvcResult = sendRequest(userDTO);
		assertThat(mvcResult.getResponse().getStatus()).isEqualTo(400);
		assertThat(AppConstants.USER_ALREADY_EXISTS.equals(mvcResult.getResponse().getContentAsString()));
				
	}

	@Test
	@DisplayName("Empty First Name")
	void testEmptyFirstName() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("Test@123");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("First Name is Required");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Empty Last Name")
	void testEmptyLastName() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("Test@123");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Last Name is Required");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Empty EMAIL Id")
	void testEmptyEmail() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("");
		userDTO.setPassword("Test@123");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Email is Required");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Invalid EMAIL Id")
	void testInvalidEmail() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("test");
		userDTO.setPassword("Test@123");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Not a Valid EMail");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	@Test
	@DisplayName("Empty Password")
	void testEmptyPassword() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Password must be 8 to 16 characters");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Short Password")
	void testShortPassword() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("Test");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Password must be 8 to 16 characters");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@DisplayName("Long Password")
	void testLongPassword() {
		UserRequestDTO userDTO = new UserRequestDTO();
		userDTO.setFirstName("Test");
		userDTO.setLastName("S");
		userDTO.setEmailId("Tests@gmail.com");
		userDTO.setPassword("TestTestTestTestTestTestTest");

		MvcResult mvcResult = sendRequest(userDTO);
		MockHttpServletResponse response = mvcResult.getResponse();
		int status = response.getStatus();
		assertThat(status).isEqualTo(400);
		try {
			assertThat(response.getContentAsString()).isEqualTo("Password must be 8 to 16 characters");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
}
