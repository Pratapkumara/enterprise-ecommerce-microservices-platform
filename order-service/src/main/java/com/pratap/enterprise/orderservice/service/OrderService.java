package com.pratap.enterprise.orderservice.service;

import com.pratap.enterprise.orderservice.dto.OrderRequest;
import com.pratap.enterprise.orderservice.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest request);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

}
