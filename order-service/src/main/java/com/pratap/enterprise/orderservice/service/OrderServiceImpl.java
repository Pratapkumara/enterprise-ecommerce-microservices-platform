package com.pratap.enterprise.orderservice.service;

import com.pratap.enterprise.orderservice.client.NotificationClient;
import com.pratap.enterprise.orderservice.client.PaymentClient;
import com.pratap.enterprise.orderservice.client.ProductClient;
import com.pratap.enterprise.orderservice.client.dto.NotificationRequest;
import com.pratap.enterprise.orderservice.client.dto.PaymentMethod;
import com.pratap.enterprise.orderservice.client.dto.PaymentRequest;
import com.pratap.enterprise.orderservice.client.dto.ProductResponse;
import com.pratap.enterprise.orderservice.dto.OrderRequest;
import com.pratap.enterprise.orderservice.dto.OrderResponse;
import com.pratap.enterprise.orderservice.entity.Order;
import com.pratap.enterprise.orderservice.entity.OrderItem;
import com.pratap.enterprise.orderservice.entity.OrderStatus;
import com.pratap.enterprise.orderservice.exception.OrderNotFoundException;
import com.pratap.enterprise.orderservice.mapper.OrderMapper;
import com.pratap.enterprise.orderservice.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    private final PaymentClient paymentClient;

    private final NotificationClient notificationClient;


    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {

        Order order = Order.builder()
                .userId(request.getUserId())
                .build();


        List<OrderItem> items = request.getItems()
                .stream()
                .map(itemRequest -> {

                    ProductResponse product =
                            productClient.getProductById(
                                    itemRequest.getProductId()
                            );

                    return OrderItem.builder()
                            .productId(product.getId())
                            .quantity(itemRequest.getQuantity())
                            .price(product.getPrice())
                            .order(order)
                            .build();

                })
                .toList();


        order.setItems(items);


        BigDecimal totalAmount = items.stream()
                .map(item ->
                        item.getPrice().multiply(
                                BigDecimal.valueOf(item.getQuantity())
                        )
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        order.setTotalAmount(totalAmount);


        Order savedOrder = orderRepository.save(order);


        paymentClient.processPayment(
                PaymentRequest.builder()
                        .orderId(savedOrder.getId())
                        .userId(savedOrder.getUserId())
                        .amount(savedOrder.getTotalAmount())
                        .paymentMethod(PaymentMethod.UPI)
                        .build()
        );


        sendNotification(savedOrder);


        return OrderMapper.toResponse(savedOrder);
    }


    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "notificationFallback"
    )
    @Retry(
            name = "notificationService"
    )
    public void sendNotification(Order savedOrder) {


        notificationClient.sendNotification(
                NotificationRequest.builder()
                        .userId(savedOrder.getUserId())
                        .email("test@gmail.com")
                        .message("Your order has been created successfully")
                        .type("ORDER")
                        .build()
        );

    }


    public void notificationFallback(
            Order savedOrder,
            Throwable throwable
    ) {

        System.out.println(
                "Notification service unavailable. Order created successfully. Reason: "
                        + throwable.getMessage()
        );

    }



    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        return OrderMapper.toResponse(order);
    }



    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(OrderMapper::toResponse)
                .toList();
    }



    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, String status) {


        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with id: " + id
                        )
                );


        order.setStatus(
                OrderStatus.valueOf(status.toUpperCase())
        );


        Order updatedOrder = orderRepository.save(order);


        return OrderMapper.toResponse(updatedOrder);
    }

}
