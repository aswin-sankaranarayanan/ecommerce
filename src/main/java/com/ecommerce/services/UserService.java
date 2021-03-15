package com.ecommerce.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.AppConstants;
import com.ecommerce.dtos.UserRequestDTO;
import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.entity.User;
import com.ecommerce.exceptions.ApplicationException;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.security.LoginDTO;
import com.ecommerce.security.LoginResponseDTO;
import com.ecommerce.security.UserDetailsImpl;
import com.ecommerce.utils.SessionUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService extends BaseService<User, UserResponseDTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Value("${token.secret}")
	private String secret;
	
	@Value("${token.expiration}")
	private long expiration;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authManger;
	
	@Autowired
	private ModelMapper mapper;
	
	@Autowired
	private PasswordEncoder encoder;

	public UserResponseDTO signUp(UserRequestDTO userDTO) {
		logger.info("Going to save user -> {}", userDTO );
		userDTO.setPassword(encoder.encode(userDTO.getPassword()));
		User user = mapper.map(userDTO, User.class);
		if(userRepository.existsByEmailId(userDTO.getEmailId())) {
			throw new ApplicationException(AppConstants.USER_ALREADY_EXISTS);
		}
		User saveduser = userRepository.save(user);
		UserResponseDTO saveduserDTO = convertToDTO(saveduser, UserResponseDTO.class);
		logger.info("Saved User Successfully -> {}", saveduserDTO );
		return saveduserDTO;
	}
	
	public LoginResponseDTO login(LoginDTO loginDTO) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getEmailId(), loginDTO.getPassword());
		try {
			Authentication authentication = authManger.authenticate(authenticationToken);
			List<String> authorities = authentication.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getUser().getId();
			LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
			Long expirationDate = System.currentTimeMillis() + expiration;
			String token = Jwts.builder()
							.setSubject(userId.toString())
							.claim("ROLE", authorities)
							.setIssuedAt(new Date())
							.setExpiration(new Date(expirationDate))
							.signWith(SignatureAlgorithm.HS512, secret.getBytes())
							.compact();
			UserResponseDTO userResponseDTO = convertToDTO(userDetails.getUser(), UserResponseDTO.class);
			loginResponseDTO.setUser(userResponseDTO);
			loginResponseDTO.setToken(token);
			loginResponseDTO.setExpiration(expirationDate);
			return loginResponseDTO;
		}catch(Exception e) {
			throw new ApplicationException("Invalid Username or Password");
		}
	}
	
	public LoginResponseDTO getLoggedInUser() {
		User loggedInUser = userRepository.findById(
								SessionUtils.getLoggedInUser().getId())
								.orElseThrow(()-> new ApplicationException("User not found!"));
		LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
		loginResponseDTO.setUser(convertToDTO(loggedInUser, UserResponseDTO.class));
		loginResponseDTO.setExpiration(System.currentTimeMillis() + expiration);
		return loginResponseDTO;
	}
}
