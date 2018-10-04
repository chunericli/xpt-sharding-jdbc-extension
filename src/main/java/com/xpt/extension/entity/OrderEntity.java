package com.xpt.extension.entity;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OrderEntity implements Serializable {

	private Long id;

	private Long orderId;

	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}