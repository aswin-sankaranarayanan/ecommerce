package com.ecommerce.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.ecommerce.entity.SubCategory;

@Repository
public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

}
