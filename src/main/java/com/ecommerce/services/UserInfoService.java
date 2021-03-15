package com.ecommerce.services;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.transaction.Transactional;
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
	
	/*
	 * 1. Is Address exists for the user goto address exists else no address
	 * 
	 * -> No Address: Just Save
	 * -> address exists 
	 * 		1. Check if incoming address is default 
	 * 			if true 
	 * 				remove other default addresses & store in list
	 * 				add the new address to list.
	 * 				save the list
	 * 			if false
	 * 				just save
	 */
	@Transactional
	public UserInfoDTO saveUserInfo(UserInfoDTO userInfoDTO) {
			User user = getLoggedInUser();
			UserInfo userInfo = convertToEntity(userInfoDTO, UserInfo.class);
			userInfo.setUser(user);
			
			List<UserInfo> userInfos = repository.findAllByUserId(user.getId());
			if(userInfos.isEmpty()) {
				userInfo.setDefaultAddress(true);
				userInfoDTO = save(userInfo);
			} else {
				if(userInfoDTO.isDefaultAddress()) {
					List<UserInfo> userInfosToUpdate = userInfos.stream().peek(pUserInfo->pUserInfo.setDefaultAddress(false)).collect(Collectors.toList());
					userInfosToUpdate.forEach(repository::save);
				}
				userInfoDTO = save(userInfo);
			}
			return userInfoDTO;
	}


	private UserInfoDTO save(UserInfo userInfo) {
		UserInfoDTO userInfoDTO;
		UserInfo savedEntity = repository.save(userInfo);
		userInfoDTO = convertToDTO(savedEntity, UserInfoDTO.class);
		return userInfoDTO;
	}


	public List<UserInfoDTO> getAllUserInfos(Long userId) {
		List<UserInfo> userInfos = repository.findAllByUserId(userId);
		List<UserInfoDTO> userInfoDTOs = userInfos.stream().map(entity->convertToDTO(entity, UserInfoDTO.class)).collect(Collectors.toUnmodifiableList());
		return userInfoDTOs;
	}

	public UserInfoDTO updateUserInfo(@Valid UserInfoDTO userInfoDTO) {
		UserInfoDTO updatedUserInfo = null;
		if(userInfoDTO.isDefaultAddress()) {
			User user = getLoggedInUser();
			List<UserInfo> userInfos = repository.findAllByUserId(user.getId());
			List<UserInfo> usersToUpdate = userInfos.stream()
							.filter(iUserInfo-> !iUserInfo.getId().equals(userInfoDTO.getId()))
							.peek(iUser-> iUser.setDefaultAddress(false))
							.collect(Collectors.toList());
			UserInfo userInfo =userInfos.stream()
							.filter(iUserInfo-> iUserInfo.getId().equals(userInfoDTO.getId()))
							.findFirst().orElseThrow(exceptionSupplier());
			copyDTOPropertiesToEntity(userInfoDTO, userInfo);
			usersToUpdate.add(userInfo);
			usersToUpdate.stream().forEach(repository::save);
			updatedUserInfo = userInfoDTO;
		}else {
			UserInfo userInfo = repository.findById(userInfoDTO.getId()).orElseThrow(exceptionSupplier());
			copyDTOPropertiesToEntity(userInfoDTO, userInfo);
			userInfo = repository.save(userInfo);
			updatedUserInfo = convertToDTO(userInfo, UserInfoDTO.class);
			
		}
		return updatedUserInfo;
	}

	public void deleteUserInfo(Long userInfoId) {
		UserInfo userInfo = repository.findById(userInfoId).orElseThrow(exceptionSupplier());
		repository.delete(userInfo);
	}
	
	public List<UserInfoDTO> getLoggedInUserInfo() {
		User user = getLoggedInUser();
		List<UserInfoDTO> userInfo = getAllUserInfos(user.getId());
		return userInfo;
	}
	
	
	private User getLoggedInUser() {
		return SessionUtils.getLoggedInUser();
	}

	private Supplier<? extends RuntimeException> exceptionSupplier() {
		return ()->new RuntimeException("Invalid UserInfo");
	}
}
