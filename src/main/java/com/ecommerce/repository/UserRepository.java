package com.ecommerce.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.ecommerce.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByEmailId(String username);

}
