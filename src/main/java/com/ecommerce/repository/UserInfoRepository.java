package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import com.ecommerce.entity.UserInfo;

public interface UserInfoRepository extends PagingAndSortingRepository<UserInfo, Long> {

	List<UserInfo> findAllByUserId(Long userId);

}
