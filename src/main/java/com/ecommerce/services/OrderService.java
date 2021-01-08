package com.ecommerce.services;

import java.util.List;
import java.util.stream.Collectors;
import static com.ecommerce.AppConstants.ORDER_STATUS;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import com.ecommerce.dtos.CartDTO;
import com.ecommerce.dtos.CartItemDTO;
import com.ecommerce.dtos.InventoryDTO;
import com.ecommerce.dtos.OrderDTO;
import com.ecommerce.dtos.OrderDetailsDTO;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.utils.SessionUtils;

@Service
public class OrderService extends BaseService<Order, OrderDTO> {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private CartService cartService;

	@Transactional
	public OrderDTO placeOrder() {
		User user = SessionUtils.getLoggedInUser();
		OrderDTO orderDTO = buildOrder();
		Order order = convertToEntity(orderDTO, Order.class);
		order.setUser(user);

		Order savedOrder = orderRepository.save(order);
		cartService.deleteCart();
		return convertToDTO(savedOrder, OrderDTO.class);
	}



	private OrderDTO buildOrder() {
		CartDTO cartDTO = cartService.getCart();
		OrderDTO orderDTO = new OrderDTO();
		List<OrderDetailsDTO> orderDetails = cartDTO.getCartItems().stream().map(this::buildOrderDetails)
				.collect(Collectors.toList());
		Double totalPrice = orderDetails.stream().map(o -> o.getPrice() * o.getQuantity()).reduce(0D, Double::sum);
		orderDTO.setOrderDetails(orderDetails);
		orderDTO.setTotalPrice(totalPrice);
		orderDTO.setStatus(ORDER_STATUS.PLACED.name());
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

	public List<OrderDTO> getPlacedOrdersByUser() {
		User user = SessionUtils.getLoggedInUser();
		List<Order> orders = orderRepository.findAllByUserAndStatusContaining(user, ORDER_STATUS.PLACED.name(),
				Sort.by(Direction.DESC, "createdDate"));
		List<OrderDTO> orderDTOs = orders.stream().map(order -> convertToDTO(order, OrderDTO.class))
				.collect(Collectors.toUnmodifiableList());
		return orderDTOs;
	}

	public List<OrderDTO> getAllOrders(String status) {
		List<Order> orders = status.equalsIgnoreCase("ALL")
				? (List<Order>) orderRepository.findAll(Sort.by(Direction.DESC, "createdDate"))
				: orderRepository.findAllByStatusContaining(status, Sort.by(Direction.DESC, "createdDate"));
		List<OrderDTO> orderDTOs = orders.stream().map(order -> convertToDTO(order, OrderDTO.class))
				.collect(Collectors.toUnmodifiableList());
		return orderDTOs;
	}

	public OrderDTO updateOrder(OrderDTO orderDTO) {
		Order order = orderRepository.findById(orderDTO.getId())
				.orElseThrow(() -> new RuntimeException("Invalid Order Id"));
		order.setStatus(orderDTO.getStatus());
		Order updatedOrder = orderRepository.save(order);
		return convertToDTO(updatedOrder, OrderDTO.class);
	}

}
