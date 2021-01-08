package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;

public interface OrderRepository extends PagingAndSortingRepository<Order, Long>{
	
	List<Order>findAllByUserAndStatusContaining(User user, String status,Sort sort);
	List<Order> findAllByStatusContaining(String status,Sort sort);

}
