package com.ecommerce.entity;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERS")
public class Order extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "USER_FK")
	private User user;
	
	@OneToOne
	@JoinColumn(name="USER_INFO_FK")
	private UserInfo userInfo;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ORDER_TX_FK")
	private OrderTransactionDetails orderTransactionDetails;

	@OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
	private List<OrderDetails> orderDetails;

	private String status;
	private Date createdDate = new Date();
	private Date estimatedDeliveryDate;
	private double totalPrice;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderDetails> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(List<OrderDetails> orderDetails) {
		this.orderDetails = orderDetails;
		this.orderDetails.stream().forEach(orderDetail->orderDetail.setOrder(this));
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public OrderTransactionDetails getOrderTransactionDetails() {
		return orderTransactionDetails;
	}

	public void setOrderTransactionDetails(OrderTransactionDetails orderTransactionDetails) {
		this.orderTransactionDetails = orderTransactionDetails;
	}
	
	
	
}
