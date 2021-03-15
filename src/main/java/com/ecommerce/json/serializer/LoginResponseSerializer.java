package com.ecommerce.json.serializer;

import java.io.IOException;

import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.security.LoginResponseDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LoginResponseSerializer extends JsonSerializer<LoginResponseDTO> {

	@Override
	public void serialize(LoginResponseDTO value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException {
		gen.writeStartObject();
		UserResponseDTO user = value.getUser();
		gen.writeStringField("emailId", user.getEmailId());
		gen.writeStringField("firstName", user.getFirstName());
		gen.writeStringField("lastName", user.getLastName());
		gen.writeStringField("token", value.getToken());
		gen.writeNumberField("id", user.getId());
		gen.writeStringField("role", user.getRole());
		gen.writeNumberField("expiration", value.getExpiration());
		gen.writeEndObject();
		
	}

}
