package com.ecommerce.dtos;

import java.util.Date;
import java.util.List;

import com.ecommerce.json.serializer.OrderSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = OrderSerializer.class)
public class OrderDTO extends BaseDTO {

	private List<OrderDetailsDTO> orderDetails;
	private Date createdDate = new Date();
	private Date estimatedDeliveryDate;
	private double totalPrice;
	private String status;
	private UserInfoDTO userInfo;
	private UserResponseDTO user;

	public List<OrderDetailsDTO> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetailsDTO> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getEstimatedDeliveryDate() {
		return estimatedDeliveryDate;
	}

	public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
		this.estimatedDeliveryDate = estimatedDeliveryDate;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public UserResponseDTO getUser() {
		return user;
	}

	public void setUser(UserResponseDTO user) {
		this.user = user;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public UserInfoDTO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoDTO userInfo) {
		this.userInfo = userInfo;
	}

	@Override
	public String toString() {
		return "OrderDTO [orderDetails=" + orderDetails + ", createdDate=" + createdDate + ", estimatedDeliveryDate="
				+ estimatedDeliveryDate + ", totalPrice=" + totalPrice + ", status=" + status + ", user=" + user + "]";
	}
	
}
