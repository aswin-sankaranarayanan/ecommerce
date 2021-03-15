package com.ecommerce.apis;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.ecommerce.TokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@PropertySource("classpath:application-test.properties")
public class BaseAPITest {
	
	@Value("${token.secret}")
	private String secret;

	@Value("${token.expiration}")
	private long expiration;

	private static String token = null;
	
	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper mapper;
	
	@BeforeEach
	public void generateToken() {
		token = TokenGenerator.getToken(1L, "USER", secret, expiration);
	}
	
	public String generateTokenForUser() {
		generateToken();
		return token;
	}

	protected MockHttpServletResponse performPost(String ENDPOINT,Object content) throws Exception {
		System.out.println("Sending Request to "+ENDPOINT);
		System.out.println("Content:"+mapper.writeValueAsString(content));
		MvcResult mvcResult = mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(mapper.writeValueAsString(content)))
				.andReturn();
		 return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performPost(String ENDPOINT) throws Exception {
		System.out.println("Sending Request to "+ENDPOINT);
		MvcResult mvcResult = mockMvc.perform(post(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token))
				.andReturn();
		 return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performGet(String ENDPOINT) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(ENDPOINT)
				.header("Authorization", "Bearer "+token))
				.andReturn();
		return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performGetByAdmin(String ENDPOINT) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get(ENDPOINT)
				.header("Authorization", "Bearer "+getTokenForAdmin()))
				.andReturn();
		return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performPut(String ENDPOINT,Object content) throws Exception {
		MvcResult mvcResult = mockMvc.perform(put(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+token)
				.content(mapper.writeValueAsString(content)))
				.andReturn();
		return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performPutByAdmin(String ENDPOINT,Object content) throws Exception {
		MvcResult mvcResult = mockMvc.perform(put(ENDPOINT)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer "+getTokenForAdmin())
				.content(mapper.writeValueAsString(content)))
				.andReturn();
		return mvcResult.getResponse();
	}
	
	protected MockHttpServletResponse performDelete(String ENDPOINT) throws Exception {
		 MvcResult mvcResult = mockMvc.perform(delete(ENDPOINT)
				.header("Authorization", "Bearer "+token))
				.andReturn();
		 return mvcResult.getResponse();
	}
	
	protected String getTokenForAdmin() {
		return TokenGenerator.getToken(3L, "ADMIN", secret, expiration);
	}
	
	protected String getTokenForUser2() {
		return TokenGenerator.getToken(2L, "USER", secret, expiration);
	}
}
