package com.ecommerce.services;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.ecommerce.AppConstants.ORDER_STATUS;
import com.ecommerce.dtos.CartDTO;
import com.ecommerce.dtos.CartItemDTO;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.dtos.OrderDetailsDTO;
import com.ecommerce.dtos.OrderResponseDTO;
import com.ecommerce.dtos.PaytmInitiateTransactionResponse;
import com.ecommerce.dtos.RazorPayResponse;
import com.ecommerce.dtos.UserInfoDTO;
import com.ecommerce.dtos.paytm.Body;
import com.ecommerce.dtos.paytm.PaytmConfig;
import com.ecommerce.dtos.paytm.PaytmTransactionRequest;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.OrderTransactionDetails;
import com.ecommerce.entity.User;
import com.ecommerce.entity.UserInfo;
import com.ecommerce.repository.OrderProjection;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserInfoRepository;
import com.ecommerce.utils.SessionUtils;
import com.google.gson.JsonObject;
import com.paytm.pg.merchant.PaytmChecksum;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Service
@Transactional
public class OrderService extends BaseService<Order, OrderDTO> {
	
	private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
	
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartService cartService;
	
	@Autowired
	private UserInfoRepository userInfoRepository;
	
	@Autowired
	private PaytmConfig paytmConfig;
	
	@Value("${delivery.charge:40}")
	private Long deliveryCharge;
	
	@Value("${razorpay.key}")
	private String key;
	
	@Value("${razorpay.secret}")
	private String secret;
	
	@Value("${razorpay.successRedirectUrl}")
	private String redirectUrl;
	
	public OrderDTO placeOrder(UserInfoDTO userInfoDTO) {
		User user = SessionUtils.getLoggedInUser();
		UserInfo userInfo = userInfoRepository.findById(userInfoDTO.getId()).orElseThrow(()-> new RuntimeException("Invalid UserInfo"));
		OrderDTO orderDTO = buildOrder();
		Order order = convertToEntity(orderDTO, Order.class);
		order.setUser(user);
		order.setUserInfo(userInfo);

		Order savedOrder = orderRepository.save(order);
		OrderDTO savedOrderDTO = convertToDTO(savedOrder, OrderDTO.class);
		return savedOrderDTO;
	}

	private OrderDTO buildOrder() {
		CartDTO cartDTO = cartService.getCart();
		OrderDTO orderDTO = new OrderDTO();
		List<OrderDetailsDTO> orderDetails = cartDTO.getCartItems().stream().map(this::buildOrderDetails)
				.collect(Collectors.toList());
		Double totalPrice = orderDetails.stream().map(o -> o.getPrice() * o.getQuantity()).reduce(0D, Double::sum);
		orderDTO.setOrderDetails(orderDetails);
		orderDTO.setTotalPrice(totalPrice+deliveryCharge);
		orderDTO.setStatus(ORDER_STATUS.PENDING.name());
		return orderDTO;
	}
	
	
	private OrderDetailsDTO buildOrderDetails(CartItemDTO cartItem) {
		OrderDetailsDTO orderDetails = new OrderDetailsDTO();
		InventoryDTO inventory = cartItem.getInventory();
		orderDetails.setItem(inventory.getItem());
		orderDetails.setPrice(inventory.getPrice());
		orderDetails.setQuantity(cartItem.getQuantity());
		return orderDetails;
	}

	public List<OrderProjection> getPlacedOrdersByUser() {
		User user = SessionUtils.getLoggedInUser();
		List<OrderProjection> orders = orderRepository.getOrdersForUser(user.getId());
		return orders;
	}

	public OrderResponseDTO getAllOrders(String status, Integer pageNumber, Integer size) {
		OrderResponseDTO orders = new OrderResponseDTO();
		Pageable pageable = PageRequest.of(pageNumber, size, Sort.by(Direction.DESC, "createdDate"));
		Page<Order> allOrders = status.equalsIgnoreCase("ALL")?orderRepository.findAll(pageable):orderRepository.findAllByStatusContaining(status, pageable);
		List<OrderDTO> orderDTOs = allOrders.stream().map(order -> convertToDTO(order, OrderDTO.class)).collect(Collectors.toUnmodifiableList());
		orders.setOrders(orderDTOs);
		orders.setCurrentPage(pageNumber+1);
		orders.setTotalElements(allOrders.getTotalElements());
		orders.setTotalPages(allOrders.getTotalPages());
		return orders;
	}

	public OrderDTO updateOrder(OrderDTO orderDTO) {
		Order order = orderRepository.findById(orderDTO.getId())
				.orElseThrow(() -> new RuntimeException("Invalid Order Id"));
		order.setStatus(orderDTO.getStatus());
		Order updatedOrder = orderRepository.save(order);
		return convertToDTO(updatedOrder, OrderDTO.class);
	}
	
	public void updateOrderStatusToPlaced(Long orderId) {
		orderRepository.upadateOrderStatusToPlaces(orderId, ORDER_STATUS.PLACED.name());
	}
	
	public PaytmTransactionRequest constructPaytmTransactionRequest(Long orderId) throws Exception {

		URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid="+paytmConfig.getMid()+"&orderId="+orderId);
		JsonObject paytmParams = new JsonObject();
		PaytmTransactionRequest request = new PaytmTransactionRequest();

		JsonObject body = new JsonObject();
		body.addProperty("requestType", "Payment");
		body.addProperty("mid", paytmConfig.getMid());
		body.addProperty("websiteName", paytmConfig.getWebsite());
		body.addProperty("orderId", orderId);
		body.addProperty("callbackUrl", paytmConfig.getCallbackUrl());

		JsonObject txnAmount = new JsonObject();
		Long price = orderRepository.findTotalPriceById(orderId);
		txnAmount.addProperty("value", String.valueOf(price+deliveryCharge));

		txnAmount.addProperty("currency", "INR");

		JsonObject userInfo = new JsonObject();
		userInfo.addProperty("custId", "CUST_001");
		body.add("txnAmount", txnAmount);
		body.add("userInfo", userInfo);

		
		String checksum = PaytmChecksum.generateSignature(body.toString(), paytmConfig.getKey());

		JsonObject head = new JsonObject();
		head.addProperty("signature", checksum);
		paytmParams.add("body", body);
		paytmParams.add("head", head);

		String post_data = paytmParams.toString();
		String token = null;
		RestTemplate restTemplate = new RestTemplate();	
		
		logger.info("Sending Request to Paytm --> "+post_data);
		RequestEntity<String> requestEntity = RequestEntity.post(url.toURI())
										.contentType(MediaType.APPLICATION_JSON)
										.body(post_data);
		ResponseEntity<PaytmInitiateTransactionResponse> responseEntity = restTemplate
								.exchange(requestEntity, PaytmInitiateTransactionResponse.class);
		
		logger.info("Response from Paytm -->"+ responseEntity.getStatusCodeValue());
		Body responseBody = responseEntity.getBody().getBody();
		
		logger.info("Response with token --> "+responseEntity.getBody());
		if(responseBody.getResultInfo().getResultStatus().equals("S")) {
			token = responseBody.getTxnToken();
			logger.info("Response with token --> "+token);
		}
		request.setToken(token);
		request.setMid(paytmConfig.getMid());
		request.setOrderId(String.valueOf(orderId));
		request.setCallbackUrl(paytmConfig.getCallbackUrl());
		
		return request;
	
	}



	public void saveOrderTransactionDetails(Map<String, String[]> map) {
		String orderId = map.get("ORDERID")[0];
		Order order = orderRepository.findById(Long.valueOf(orderId)).orElseThrow(()-> new RuntimeException("Invalid Order ID"));
		OrderTransactionDetails txnDetails = new OrderTransactionDetails();
		txnDetails.setTxnAmount(map.get("TXNAMOUNT")[0]);
		txnDetails.setCurrency(map.get("CURRENCY")[0]);
		txnDetails.setTxnDate(map.get("TXNDATE")[0]);
		txnDetails.setStatus(map.get("STATUS")[0]);
		txnDetails.setRespMsg(map.get("RESPMSG")[0]);
		txnDetails.setBankTXID(map.get("BANKTXNID")[0]);
		txnDetails.setBankName(map.get("BANKNAME")[0]);
		order.setOrderTransactionDetails(txnDetails);
		orderRepository.save(order);
	}



	public List<OrderProjection> getOrderDetails(Long id) {
		List<OrderProjection> orderDetails = orderRepository.getOrderDetails(id);
		return orderDetails;
	}
	
	public User getCreatedUser(Long orderId) {
	  Order order = orderRepository.findById(orderId).orElseThrow(()->new RuntimeException("Invalid Order Id"));
	  User user = order.getUser();
	  return user;
	}
	
	public OrderTransactionDetails getTransactionDetails(Long id) {
		Order  order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Invalid Order Id"));
		return order.getOrderTransactionDetails();
	}

	public RazorPayResponse placeRazorPayOrder(UserInfoDTO userInfo) throws RazorpayException {
		OrderDTO orderDTO = placeOrder(userInfo);
		RazorpayClient razorpay = new RazorpayClient(key,secret);
		RazorPayResponse response = new RazorPayResponse();
		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", orderDTO.getTotalPrice()*100); // amount in the smallest currency unit
		orderRequest.put("currency", "INR");
		//orderRequest.put("receipt", orderDTO.getId());

		com.razorpay.Order order = razorpay.Orders.create(orderRequest);
		logger.info("Razor Pay Order -->"+ order.get("id")+ order.get("status"));
		response.setId(order.get("id"));
		response.setStatus(order.get("status"));
		response.setTotalPrice(orderDTO.getTotalPrice()*100); // *100 => 2573 = 25.73 converted by payment gateway
		response.setRedirectUrl(redirectUrl+"/"+orderDTO.getId());
		response.setKey(key);
		return response;
	}

}
