package com.ecommerce.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.ecommerce.entity.CartItem;

public interface CartItemRepository extends PagingAndSortingRepository<CartItem, Long> {

}
