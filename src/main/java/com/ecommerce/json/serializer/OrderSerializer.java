package com.ecommerce.json.serializer;

import java.io.IOException;
import com.ecommerce.dtos.OrderDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class OrderSerializer extends JsonSerializer<OrderDTO> {

	@Override
	public void serialize(OrderDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeNumberField("price", value.getTotalPrice());
		if(value.getUser()!=null)
		gen.writeStringField("user", value.getUser().getFirstName() + " "+ value.getUser().getLastName());
		gen.writeStringField("status", value.getStatus());
		gen.writeStringField("purchaseDate", value.getCreatedDate().toString());
		gen.writeEndObject();
	}

}
