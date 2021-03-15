package com.ecommerce.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.ecommerce.entity.User;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

	User findByEmailId(String emailId);
	boolean existsByEmailId(String emailId);
	@Query(value = "select emailId from User where id=:id")
	String findEmailId(@Param("id")Long id);

}
