package com.ecommerce.apis;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryImageDTO;
import com.ecommerce.services.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryAPI {
	
	@Autowired
	private InventoryService inventoryService;

	
	@GetMapping
	public ResponseEntity<Iterable<InventoryDTO>> getInventory() {
		Iterable<InventoryDTO> items = inventoryService.getInventory();
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<InventoryDTO> getItemFromInventory(@PathVariable("id") Long id) {
		InventoryDTO inventoryDTO = inventoryService.getItemFromInventory(id);
		return ResponseEntity.ok(inventoryDTO);
	}
	
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<InventoryDTO> addToInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
		InventoryDTO item = inventoryService.saveInventory(inventoryDTO);
		return ResponseEntity.ok(item);
	}
	
	@PostMapping(value="/image",consumes ="multipart/form-data")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<InventoryDTO> addImage(@RequestParam("inventoryImages") List<MultipartFile> files
												 ,@RequestParam("id") Long inventoryId) {
		InventoryDTO inventoryDTO = new InventoryDTO();
		inventoryDTO.setId(1L);
		
		for(MultipartFile file : files){
			InventoryImageDTO inventoryImage = new InventoryImageDTO();
			inventoryImage.setFileName(file.getOriginalFilename());
			try {
				inventoryImage.setImage(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
			inventoryDTO.addInventoryImage(inventoryImage);	
		}

		InventoryDTO savedInventory = inventoryService.saveImage(inventoryDTO);
		return ResponseEntity.ok(savedInventory);
	}
	
	@PutMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<InventoryDTO> updateInventory(@RequestBody @Valid InventoryDTO inventoryDTO) {
		InventoryDTO item = inventoryService.updateInventory(inventoryDTO);
		return ResponseEntity.ok(item);
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> updateInventory(@PathVariable("id") Long id) {
		inventoryService.softDeleteItem(id);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/image/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteInventoryImage(@PathVariable("id") Long id) {
		inventoryService.deleteInventoryImage(id);
		return ResponseEntity.ok().build();
	}
	
}
