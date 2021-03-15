
package com.ecommerce.apis;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.dtos.UserResponseDTO;
import com.ecommerce.dtos.paytm.PaytmConfig;
import com.ecommerce.dtos.paytm.PaytmTransactionRequest;
import com.ecommerce.entity.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.services.CartService;
import com.ecommerce.services.MailService;
import com.ecommerce.services.OrderService;
import com.ecommerce.utils.SessionUtils;


@RestController
@RequestMapping("/api/checkout")
public class CheckoutAPI {
	
	@Autowired
	private PaytmConfig paytmConfig;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private CartService cartService;
	
	@Value("${payment.success}")
	private String paymentSuccessUrl;
	

	private static final Logger logger = LoggerFactory.getLogger(CheckoutAPI.class);
	
	
	@PostMapping(value ="/callback/{id}")
	public void handleCallbackPost1(HttpServletRequest request, HttpServletResponse response,@PathVariable("id")Long orderId) throws IOException {
		User user  = orderService.getCreatedUser(orderId);
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		Map<String, String[]> parameterMap = request.getParameterMap();
		parameterMap.forEach((k,v) -> logger.info("Key "+k +" "+"Value "+v[0]));
		orderService.updateOrderStatusToPlaced(orderId);
		cartService.deleteCart(user);
		mailService.sendEmail(user,orderId);
		redirectStrategy.sendRedirect(request, response, paymentSuccessUrl);
	}
	
	
	
	/* Uncomment for Paytm
	 
	@PostMapping(value ="/callback")
	public void handleCallbackPost1(HttpServletRequest request, HttpServletResponse response) throws IOException {
		RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
		Map<String, String[]> parameterMap = request.getParameterMap();
		parameterMap.forEach((k,v) -> logger.info("Key "+k +" "+"Value "+v[0]));
		orderService.saveOrderTransactionDetails(parameterMap);
		if(parameterMap.containsKey("STATUS")) {
			if(parameterMap.get("STATUS")[0].equals("TXN_SUCCESS")) {
				Long orderId = Long.parseLong(parameterMap.get("ORDERID")[0]);
				orderService.updateOrderStatusToPlaced(orderId);
				cartService.deleteCart(user);
				String email = userRepository.findEmailId(user.getId());
				mailService.sendEmail(email,orderId);
				redirectStrategy.sendRedirect(request, response, paytmConfig.getSuccessRedirectUrl());
			}else {
				redirectStrategy.sendRedirect(request, response, paytmConfig.getFailureRedirectUrl());
			}
		}
		redirectStrategy.sendRedirect(request, response, paytmConfig.getSuccessRedirectUrl());
	}
	*/
	

	@GetMapping("/{orderId}")
	public ResponseEntity<PaytmTransactionRequest> checkout(@PathVariable("orderId") Long orderId) throws Exception {
		PaytmTransactionRequest request = orderService.constructPaytmTransactionRequest(orderId);
		return ResponseEntity.ok(request);
	}
}
