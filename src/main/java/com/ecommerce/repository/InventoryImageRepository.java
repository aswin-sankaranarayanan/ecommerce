package com.ecommerce.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ecommerce.entity.InventoryImage;

public interface InventoryImageRepository extends PagingAndSortingRepository<InventoryImage, Long> {

}
