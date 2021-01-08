package com.ecommerce.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ecommerce.entity.Inventory;

public interface InventoryRepository extends PagingAndSortingRepository<Inventory, Long> {

}
