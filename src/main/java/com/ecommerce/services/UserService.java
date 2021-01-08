package com.ecommerce.services;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.dtos.UserRequestDTO;
import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;

@Service
public class UserService extends BaseService<User, UserResponseDTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;

	public UserResponseDTO signUp(UserRequestDTO userDTO) {
		logger.info("Going to save user -> {}", userDTO );
		userDTO.setPassword(encoder.encode(userDTO.getPassword()));
		User user = mapper.map(userDTO, User.class);
		User saveduser = userRepository.save(user);
		UserResponseDTO saveduserDTO = convertToDTO(saveduser, UserResponseDTO.class);
		logger.info("Saved User Successfully -> {}", saveduserDTO );
		return saveduserDTO;
	}
}
