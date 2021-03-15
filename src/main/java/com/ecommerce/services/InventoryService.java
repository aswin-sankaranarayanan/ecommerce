package com.ecommerce.services;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryImageDTO;
import com.ecommerce.dtos.InventoryResponseDTO;
import com.ecommerce.dtos.PagedResponseDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Inventory;
import com.ecommerce.entity.InventoryImage;
import com.ecommerce.entity.SubCategory;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.InventoryImageRepository;
import com.ecommerce.repository.InventoryProjection;
import com.ecommerce.repository.InventoryRepository;
import com.ecommerce.repository.SubCategoryRepository;

@Service
public class InventoryService extends BaseService<Inventory, InventoryDTO> {

	@Autowired
	private InventoryRepository repository;

	@Autowired
	private InventoryImageRepository imageRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private SubCategoryRepository subCategoryRepository;

	@Autowired
	private AWSUtilityService awsUtilityService;

	public InventoryDTO saveInventory(InventoryDTO inventoryDTO) {
		Inventory inventory = convertToEntity(inventoryDTO, Inventory.class);
		Inventory savedEntity = repository.save(inventory);
		InventoryDTO savedDTO = convertToDTO(savedEntity, InventoryDTO.class);
		return savedDTO;
	}

	public InventoryResponseDTO getInventory(int pageNum, int size, String filter) {
		var inventoryResponseDTO = new InventoryResponseDTO();
		Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Direction.ASC, "item"));
		Page<Inventory> page = filter.isBlank() ? repository.findAll(pageable)
				: repository.filterInvetory(filter, pageable);

		List<InventoryDTO> inventoryDTOS = page.getContent().stream()
				.map(entity -> convertToDTO(entity, InventoryDTO.class)).collect(Collectors.toList());

		inventoryResponseDTO.setCurrentPage(pageNum + 1);
		inventoryResponseDTO.setTotalPages(page.getTotalPages());
		inventoryResponseDTO.setTotalElements(page.getTotalElements());
		inventoryResponseDTO.setInventory(inventoryDTOS);
		return inventoryResponseDTO;
	}

	public InventoryDTO getItemFromInventory(Long id) {
		Inventory inventoryEntity = findById(id);
		InventoryDTO inventoryDTO = convertToDTO(inventoryEntity, InventoryDTO.class);
		return inventoryDTO;
	}

	public InventoryDTO updateInventory(InventoryDTO inventoryDTO) {
		Inventory item = findById(inventoryDTO.getId());
		Category category = categoryRepository.findById(inventoryDTO.getCategory().getId())
				.orElseThrow(() -> new RuntimeException("Invalid Category"));

		SubCategory subCategory = subCategoryRepository.findById(inventoryDTO.getSubCategory().getId())
				.orElseThrow(() -> new RuntimeException("Invalid Sub Category"));
		item.setCategory(category);
		item.setSubCategory(subCategory);
		copyDTOPropertiesToEntity(inventoryDTO, item);
		Inventory savedInventory = repository.save(item);
		InventoryDTO savedInventoryDTO = convertToDTO(savedInventory, InventoryDTO.class);
		return savedInventoryDTO;
	}

	public void softDeleteItem(Long id) {
		Inventory item = findById(id);
		item.setAvailable(false);
		repository.save(item);
	}

	public InventoryDTO saveImage(InventoryDTO inventoryDTO) {
		Inventory inventory = findById(inventoryDTO.getId());

		for (InventoryImageDTO inventoryImageDTO : inventoryDTO.getInventoryImages()) {
			InventoryImage inventoryImage = new InventoryImage();
			inventoryImage.setFileName(inventoryImageDTO.getFileName());
			inventory.addInventoryImage(inventoryImage);
		}

		Inventory savedInventory = repository.save(inventory);
		return convertToDTO(savedInventory, InventoryDTO.class);
	}

	private Inventory findById(Long id) {
		Inventory item = repository.findById(id).orElseThrow(() -> new RuntimeException("Item Not Found"));
		return item;
	}

	public void deleteInventoryImage(Long id) {
		InventoryImage image = imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Image Not Found"));
		awsUtilityService.deleteFileInS3(image.getFileName());
		imageRepository.deleteById(id);
	}

	public Map<String, List<InventoryProjection>> getInventoryByCategory(Long categoryId) {
		List<InventoryProjection> items = (List<InventoryProjection>) repository.getInventoryProjection(categoryId);
		Map<String, List<InventoryProjection>> map = items.stream()
				.collect(Collectors.groupingBy(InventoryProjection::getSubCategory));
		return map;
	}

	public InventoryDTO getItem(String item) {
		Inventory inventory = repository.findByItem(item).orElseThrow(() -> new RuntimeException("Invalid Item"));
		return convertToDTO(inventory, InventoryDTO.class);
	}

	public PagedResponseDTO<InventoryProjection> getItemsBySubCategory(String subCategory, int page, int size) {
		PagedResponseDTO<InventoryProjection> response = new PagedResponseDTO<InventoryProjection>();
		Pageable pageable = PageRequest.of(page, size);
		Page<InventoryProjection> items = repository.getItemsBySubCategory(subCategory, pageable);
		response.setCurrentPage(page + 1);
		response.setTotalElements(items.getTotalElements());
		response.setTotalPages(items.getTotalPages());
		response.setItems(items.getContent());
		return response;
	}
	
	public List<InventoryProjection> searchItem(String searchString){
		List<InventoryProjection> items = new LinkedList<InventoryProjection>();
		items = repository.searchItem("%"+searchString+"%");
		return items;
	}

}
