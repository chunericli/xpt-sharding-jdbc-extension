package com.xpt.extension.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xpt.extension.entity.OrderEntity;
import com.xpt.extension.mapper.OrderMapper;

@Service
public class OrderService {

	@Autowired
	private OrderMapper orderMapper;

	public int createOrder(OrderEntity order) {
		return orderMapper.createOrder(order);
	}
}