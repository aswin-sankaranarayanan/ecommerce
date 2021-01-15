package com.ecommerce.apis;

import static com.ecommerce.AppConstants.USER_SIGNUP_SUCCESS;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.dtos.ResponseDTO;
import com.ecommerce.dtos.UserRequestDTO;
import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.security.LoginDTO;
import com.ecommerce.security.UserDetailsImpl;
import com.ecommerce.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@RequestMapping("/api/user-management")
public class UserManagementAPI {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authManger;
	
	@Value("${token.secret}")
	private String secret;
	
	@Value("${token.expiration}")
	private long expiration;


	@PostMapping("/signup")
	public ResponseEntity<ResponseDTO> signup(@Valid @RequestBody UserRequestDTO userDTO) {
		UserResponseDTO savedUserDTO = userService.signUp(userDTO);
		ResponseDTO responseDTO = new ResponseDTO(USER_SIGNUP_SUCCESS, savedUserDTO);
		return ResponseEntity.ok(responseDTO);
	}
	
	@PostMapping("/email/login")
	public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginDTO.getEmailId(), loginDTO.getPassword());
		try {
			Authentication authentication = authManger.authenticate(authenticationToken);
			List<String> authorities = authentication.getAuthorities().stream().map(authority-> authority.getAuthority()).collect(Collectors.toList());
			UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
			Long userId = userDetails.getUser().getId();
			String token = Jwts.builder()
							.setSubject(userId.toString())
							.claim("ROLE", authorities)
							.setIssuedAt(new Date())
							.setExpiration(new Date(System.currentTimeMillis() + expiration))
							.signWith(SignatureAlgorithm.HS512, secret.getBytes())
							.compact();
			return ResponseEntity.ok(token);
		}catch(Exception e) {
			return ResponseEntity.status(400).body(e.getMessage());
		}
		
		
		
	}
	

}
