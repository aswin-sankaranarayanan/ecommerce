package com.ecommerce.services;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ecommerce.dtos.CategoryDTO;
import com.ecommerce.dtos.SubCategoryDTO;

@SpringBootTest
class CategoryServiceTest {

	@Autowired
	private CategoryService categoryService;
	
	@Test
	@Transactional
	void testGetAllCategories() {
		List<CategoryDTO> allCategories = categoryService.getAllCategories();
		assertThat(allCategories).isNotEmpty();
	}
	
	@Test
	@Transactional
	void testGetAllSubCategories() {
		List<SubCategoryDTO> subCategories = categoryService.getAllSubCategories(1L);
		assertThat(subCategories).hasSize(7);
	}

}
