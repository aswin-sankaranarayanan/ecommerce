package com.ecommerce.apis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryImageDTO;
import com.ecommerce.dtos.InventoryResponseDTO;
import com.ecommerce.dtos.PagedResponseDTO;
import com.ecommerce.dtos.SubCategoryDTO;
import com.ecommerce.repository.InventoryProjection;
import com.ecommerce.services.AWSUtilityService;
import com.ecommerce.services.CartService;
import com.ecommerce.services.CategoryService;
import com.ecommerce.services.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryAPI {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private AWSUtilityService awsUtilityService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private CartService cartService;

	
	@GetMapping
	public ResponseEntity<InventoryResponseDTO> getInventory(@RequestParam("page")int page, @RequestParam("size")int size,
			@RequestParam(name = "filter",defaultValue = "",required=false) String filter) {
		InventoryResponseDTO items = inventoryService.getInventory(page,size,filter);
		return ResponseEntity.ok(items);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<InventoryDTO> getItemFromInventory(@PathVariable("id") Long id) {
		InventoryDTO inventoryDTO = inventoryService.getItemFromInventory(id);
		return ResponseEntity.ok(inventoryDTO);
	}

	
	@PostMapping(consumes = {"multipart/form-data"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<InventoryDTO> addToInventory(@RequestParam("images") List<MultipartFile> files,
														@RequestParam("item") String item,
														@RequestParam("description") String description,
														@RequestParam("category") Long categoryId,
														@RequestParam("subCategory") Long subCategoryId,
														@RequestParam("available") Boolean available,
														@RequestParam("price") Double price,
														@RequestParam("language") String language) throws Exception{
		
				CategoryDTO categoryDTO =  new CategoryDTO(categoryId);
				SubCategoryDTO subCategoryDTO = new SubCategoryDTO(subCategoryId);
				
				InventoryDTO inventoryDTO = new InventoryDTO();
				inventoryDTO.setItem(item);
				inventoryDTO.setDescription(description);
				inventoryDTO.setCategory(categoryDTO);
				inventoryDTO.setAvailable(available);
				inventoryDTO.setPrice(price);
				inventoryDTO.setSubCategory(subCategoryDTO);
				
				addInventoryImages(files, inventoryDTO);
				InventoryDTO savedInventory = inventoryService.saveInventory(inventoryDTO);
				return ResponseEntity.ok(savedInventory);		
	}
	
	
	private void addInventoryImages(List<MultipartFile> files, InventoryDTO inventoryDTO) throws IOException {
		for(MultipartFile file : files){
			InventoryImageDTO inventoryImage = new InventoryImageDTO();
			inventoryImage.setFileName(file.getOriginalFilename());
			inventoryImage.setImagePath(awsUtilityService.uploadFilesToS3(file));
			inventoryDTO.addInventoryImage(inventoryImage);	
		}
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
	
	@PostMapping(value = "/image",consumes = {"multipart/form-data"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<InventoryDTO> addInventoryImages(@RequestParam("id") Long inventoryId,
			@RequestParam("images") List<MultipartFile> files) throws IOException {
		InventoryDTO inventoryDTO = inventoryService.getItemFromInventory(inventoryId);
		inventoryDTO.setId(inventoryId);
		for(MultipartFile file : files){
			InventoryImageDTO inventoryImage = new InventoryImageDTO();
			inventoryImage.setFileName(file.getOriginalFilename());
			inventoryImage.setImagePath(awsUtilityService.uploadFilesToS3(file));
			inventoryDTO.getInventoryImages().add(inventoryImage);	
		}
		InventoryDTO savedInventory = inventoryService.saveInventory(inventoryDTO);
		return ResponseEntity.ok(savedInventory);		
	}
	
	@DeleteMapping("/image/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Void> deleteInventoryImage(@PathVariable("id") Long id) {
		inventoryService.deleteInventoryImage(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/category")
	public ResponseEntity<Iterable<CategoryDTO>> getAllCategory(){
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
	
	@GetMapping("/subcategory")
	public ResponseEntity<List<SubCategoryDTO>> getSubCategory(@RequestParam("category") Long categoryId){
		return ResponseEntity.ok(categoryService.getAllSubCategories(categoryId));
	}
	
	@GetMapping("/list/category/{id}")
	public ResponseEntity<Map<String,List<InventoryProjection>>> getItemsByCategory(@PathVariable(name = "id",required=false) Long categoryId){
		Map<String, List<InventoryProjection>> category = inventoryService.getInventoryByCategory(categoryId);
		System.out.println(category);
		return ResponseEntity.ok(category);
	}

	@GetMapping("/home")
	public ResponseEntity<Map<String,Object>> getHomePageContents(@PathVariable(name = "id",required=false) Long categoryId){
		if(categoryId == null) {
			categoryId=1L;
		}
		Map<String,Object> viewData = new HashMap<String,Object>();
		viewData.put("inventory", inventoryService.getInventoryByCategory(categoryId));
		viewData.put("cart", cartService.getCartItemsCount());
		return ResponseEntity.ok(viewData);
	}
	
	@GetMapping("/view")
	public ResponseEntity<InventoryDTO> getItem(@RequestParam("item")String item){
		InventoryDTO inventoryDTO = inventoryService.getItem(item);
		return ResponseEntity.ok(inventoryDTO);
	}
	
	@GetMapping("/subcategory/view")
	public PagedResponseDTO<InventoryProjection> getItemsBySubCategory(@RequestParam("subcategory") String subCategory,
			@RequestParam("page")int page, @RequestParam(name = "size",defaultValue = "5")int size) {
		PagedResponseDTO<InventoryProjection> pagedResponseDTO = inventoryService.getItemsBySubCategory(subCategory,page,size);
		return pagedResponseDTO;
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<InventoryProjection>> searchInventory(@RequestParam("searchString") String searchString){
		List<InventoryProjection> items = inventoryService.searchItem(searchString);
		return ResponseEntity.ok(items);
	}
	
	
}
