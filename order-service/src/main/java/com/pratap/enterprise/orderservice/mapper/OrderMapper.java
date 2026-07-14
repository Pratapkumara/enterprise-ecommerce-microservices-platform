package com.pratap.enterprise.orderservice.mapper;

import com.pratap.enterprise.orderservice.dto.OrderItemResponse;
import com.pratap.enterprise.orderservice.dto.OrderResponse;
import com.pratap.enterprise.orderservice.entity.Order;
import com.pratap.enterprise.orderservice.entity.OrderItem;

import java.util.List;

public class OrderMapper {


    public static OrderResponse toResponse(Order order) {

        List<OrderItemResponse> items = order.getItems()
                .stream()
                .map(OrderMapper::toItemResponse)
                .toList();


        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(items)
                .build();
    }


    private static OrderItemResponse toItemResponse(OrderItem item) {

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }

}
