package com.ecommerce.json.serializer;

import java.io.IOException;
import java.util.List;

import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryImageDTO;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class InventorySerializer extends JsonSerializer<InventoryDTO> {

	@Override
	public void serialize(InventoryDTO value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();
		gen.writeNumberField("id", value.getId());
		gen.writeStringField("item", value.getItem());
		gen.writeStringField("description", value.getDescription());
		gen.writeNumberField("price", value.getPrice());
		gen.writeNumberField("category", value.getCategory().getId());
		gen.writeNumberField("subCategory", value.getSubCategory().getId());
		gen.writeBooleanField("available", value.isAvailable());
		serializeInnerObject(gen,value.getInventoryImages());
		gen.writeEndObject();		
	}
	
	void serializeInnerObject(JsonGenerator gen, List<InventoryImageDTO> imagesDTOs) throws IOException {
		gen.writeArrayFieldStart("inventoryImages");
		for (InventoryImageDTO inventoryImageDTO : imagesDTOs) {
			gen.writeStartObject();
			gen.writeNumberField("id", inventoryImageDTO.getId());
			gen.writeStringField("fileName", inventoryImageDTO.getFileName());
			gen.writeStringField("image", inventoryImageDTO.getImagePath());
			gen.writeEndObject();
		}
		gen.writeEndArray();
	}

}
