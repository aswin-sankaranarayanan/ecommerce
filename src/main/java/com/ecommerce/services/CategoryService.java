package com.ecommerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.SubCategoryDTO;
import com.ecommerce.entity.Category;
import com.ecommerce.repository.CategoryRepository;

@Service
@Transactional
public class CategoryService extends BaseService<Category,CategoryDTO>{

	@Autowired
	private CategoryRepository categoryRepository;
	
	public List<CategoryDTO> getAllCategories(){
		List<CategoryDTO> categoryDTOs = new ArrayList<CategoryDTO>();
		List<Category> catogories = (List<Category>) categoryRepository.findAll();
		categoryDTOs = catogories.stream()
						.map(category-> convertToDTO(category, CategoryDTO.class))
						.collect(Collectors.toUnmodifiableList());
		return categoryDTOs;
	}
	
	
	public List<SubCategoryDTO> getAllSubCategories(Long categoryId){
		List<SubCategoryDTO> subcategoryDTOs = new ArrayList<SubCategoryDTO>();
		Category catogory =  categoryRepository.findById(categoryId).orElseThrow(()->new RuntimeException("Invalid Category"));
		subcategoryDTOs = catogory.getSubCategories().stream()
						 	.map(subcategory ->  new SubCategoryDTO(subcategory.getId(),subcategory.getSubCategory()))
						 	.collect(Collectors.toUnmodifiableList());
		return subcategoryDTOs;
	}
}
