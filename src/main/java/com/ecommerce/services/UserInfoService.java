package com.ecommerce.services;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.dtos.UserInfoDTO;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserInfo;
import com.ecommerce.repository.UserInfoRepository;
import com.ecommerce.utils.SessionUtils;

@Service
public class UserInfoService extends BaseService<UserInfo, UserInfoDTO> {

	@Autowired
	private UserInfoRepository repository;
	
	public UserInfoDTO saveUserInfo(UserInfoDTO userInfoDTO) {
		UserInfo entity = convertToEntity(userInfoDTO, UserInfo.class);
		User user = SessionUtils.getLoggedInUser();
		entity.setUser(user);
		UserInfo savedEntity = repository.save(entity);
		return convertToDTO(savedEntity, UserInfoDTO.class);
	}

	public Iterable<UserInfoDTO> getAllUserInfos(Long userId) {
		List<UserInfo> userInfos = repository.findAllByUserId(userId);
		List<UserInfoDTO> userInfoDTOs = userInfos.stream().map(entity->convertToDTO(entity, UserInfoDTO.class)).collect(Collectors.toUnmodifiableList());
		return userInfoDTOs;
	}

	public UserInfoDTO updateUserInfo(@Valid UserInfoDTO userInfoDTO) {
		UserInfo userInfo = repository.findById(userInfoDTO.getId()).orElseThrow(exceptionSupplier());
		copyDTOPropertiesToEntity(userInfoDTO, userInfo);
		UserInfo updatedUserInfo = repository.save(userInfo);
		return convertToDTO(updatedUserInfo, UserInfoDTO.class);
	}

	public void deleteUserInfo(Long userInfoId) {
		UserInfo userInfo = repository.findById(userInfoId).orElseThrow(exceptionSupplier());
		repository.delete(userInfo);
	}

	private Supplier<? extends RuntimeException> exceptionSupplier() {
		return ()->new RuntimeException("Invalid UserInfo");
	}
}
