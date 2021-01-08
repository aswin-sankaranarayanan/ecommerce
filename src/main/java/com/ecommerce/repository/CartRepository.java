package com.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ecommerce.entity.Cart;
import com.ecommerce.entity.User;

public interface CartRepository extends PagingAndSortingRepository<Cart, Long> {

	Optional<Cart> findByUser(User user);

}
