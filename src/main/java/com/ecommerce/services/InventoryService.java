package com.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.InventoryImageDTO;
import com.ecommerce.entity.Inventory;
import com.ecommerce.entity.InventoryImage;
import com.ecommerce.repository.InventoryImageRepository;
import com.ecommerce.repository.InventoryRepository;

@Service
public class InventoryService extends BaseService<Inventory, InventoryDTO>{
	

	@Autowired
	private InventoryRepository repository;
	
	@Autowired
	private InventoryImageRepository imageRepository;
	
	
	public InventoryDTO saveInventory(InventoryDTO inventoryDTO) {
		Inventory inventory = convertToEntity(inventoryDTO,Inventory.class);
		Inventory savedEntity = repository.save(inventory);
		InventoryDTO savedDTO = convertToDTO(savedEntity,InventoryDTO.class);
		return savedDTO;
	}
	
	public Iterable<InventoryDTO> getInventory(){
		List<Inventory> inventoryEntity = (List<Inventory>) repository.findAll();
		List<InventoryDTO> inventoryDTOS = inventoryEntity.stream()
											.map(entity -> convertToDTO(entity,InventoryDTO.class))
											.collect(Collectors.toUnmodifiableList());
		return inventoryDTOS;
	}
	
	public InventoryDTO getItemFromInventory(Long id){
		Inventory inventoryEntity =  findById(id);
		InventoryDTO inventoryDTO = convertToDTO(inventoryEntity, InventoryDTO.class);
		return inventoryDTO;
	}
	
	public InventoryDTO updateInventory(InventoryDTO inventoryDTO) {
		Inventory item = findById(inventoryDTO.getId());
		copyDTOPropertiesToEntity(inventoryDTO, item);
		repository.save(item);
		return inventoryDTO;
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
			inventoryImage.setImage(inventoryImageDTO.getImage());
			inventory.addInventoryImage(inventoryImage);
		}
		
		Inventory savedInventory = repository.save(inventory);
		return convertToDTO(savedInventory, InventoryDTO.class);
	}
	
	private Inventory findById(Long id) {
		Inventory item = repository.findById(id).orElseThrow(()->  new RuntimeException("Item Not Found"));
		return item;
	}

	public void deleteInventoryImage(Long id) {
		imageRepository.deleteById(id);
	}
	
}
