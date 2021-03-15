package com.ecommerce.apis;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dtos.UserInfoDTO;
import com.ecommerce.services.UserInfoService;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoAPI {
	
	@Autowired
	private UserInfoService userInfoService;

	@PostMapping
	public ResponseEntity<UserInfoDTO> saveUserInfo(@RequestBody @Valid UserInfoDTO userInfoDTO){
		UserInfoDTO savedUserInfo = userInfoService.saveUserInfo(userInfoDTO);
		return ResponseEntity.ok(savedUserInfo);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<UserInfoDTO>> getUserInfo(){
		Iterable<UserInfoDTO> userInfo = userInfoService.getLoggedInUserInfo();
		return ResponseEntity.ok(userInfo);
	}
 	
	@GetMapping("/{id}")
	public ResponseEntity<Iterable<UserInfoDTO>> getAllUserInfo(@PathVariable("id") Long userId){
		Iterable<UserInfoDTO> userInfos = userInfoService.getAllUserInfos(userId);
		return ResponseEntity.ok(userInfos);
	}
	
	@PutMapping
	public ResponseEntity<UserInfoDTO> updateUserInfo(@RequestBody @Valid UserInfoDTO userInfoDTO){
		UserInfoDTO savedUserInfo = userInfoService.updateUserInfo(userInfoDTO);
		return ResponseEntity.ok(savedUserInfo);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUserInfo(@PathVariable("id") Long userInfoId){
		userInfoService.deleteUserInfo(userInfoId);
		return ResponseEntity.ok().build();
	}
}
