package com.xpt.extension.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.xpt.extension.entity.OrderEntity;
import com.xpt.extension.service.OrderService;

@Service
@RestController
public class OrderController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "createOrder")
	public int createOrder(@RequestParam(value = "userId") Long userId, @RequestParam(value = "orderId") Long orderId) {
		OrderEntity order = new OrderEntity();
		order.setUserId(userId);
		order.setOrderId(orderId);
		return orderService.createOrder(order);
	}
}