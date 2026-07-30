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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl
        implements OrderService {

    private static final String ORDER_NOT_FOUND =
            "Order not found with id: ";

    private static final String NOTIFICATION_EMAIL =
            "test@gmail.com";

    private static final String NOTIFICATION_MESSAGE =
            "Your order has been created successfully";

    private static final String NOTIFICATION_TYPE =
            "ORDER";

    private final OrderRepository orderRepository;

    private final ProductClient productClient;

    private final PaymentClient paymentClient;

    private final NotificationClient notificationClient;

    @Override
    @Transactional
    public OrderResponse createOrder(
            OrderRequest request) {

        Order order = Order.builder()
                .userId(request.getUserId())
                .build();

        List<OrderItem> items =
                createOrderItems(
                        request,
                        order
                );

        order.setItems(items);

        BigDecimal totalAmount =
                calculateTotalAmount(items);

        order.setTotalAmount(totalAmount);

        Order savedOrder =
                orderRepository.save(order);

        processPayment(savedOrder);

        sendNotification(savedOrder);

        return OrderMapper.toResponse(savedOrder);
    }

    private List<OrderItem> createOrderItems(
            OrderRequest request,
            Order order) {

        return request.getItems()
                .stream()
                .map(itemRequest -> {
                    ProductResponse product =
                            productClient.getProductById(
                                    itemRequest.getProductId()
                            );

                    return OrderItem.builder()
                            .productId(product.getId())
                            .quantity(
                                    itemRequest.getQuantity()
                            )
                            .price(product.getPrice())
                            .order(order)
                            .build();
                })
                .toList();
    }

    private BigDecimal calculateTotalAmount(
            List<OrderItem> items) {

        return items.stream()
                .map(item ->
                        item.getPrice().multiply(
                                BigDecimal.valueOf(
                                        item.getQuantity()
                                )
                        )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    private void processPayment(Order savedOrder) {
        PaymentRequest paymentRequest =
                PaymentRequest.builder()
                        .orderId(savedOrder.getId())
                        .userId(savedOrder.getUserId())
                        .amount(savedOrder.getTotalAmount())
                        .paymentMethod(PaymentMethod.UPI)
                        .build();

        paymentClient.processPayment(paymentRequest);
    }

    @CircuitBreaker(
            name = "notificationService",
            fallbackMethod = "notificationFallback"
    )
    @Retry(name = "notificationService")
    public void sendNotification(Order savedOrder) {
        NotificationRequest notificationRequest =
                NotificationRequest.builder()
                        .userId(savedOrder.getUserId())
                        .email(NOTIFICATION_EMAIL)
                        .message(NOTIFICATION_MESSAGE)
                        .type(NOTIFICATION_TYPE)
                        .build();

        notificationClient.sendNotification(
                notificationRequest
        );
    }

    public void notificationFallback(
            Order savedOrder,
            Throwable throwable) {

        log.warn(
                "Notification unavailable for order {}. "
                        + "Order creation succeeded. Reason: {}",
                savedOrder.getId(),
                throwable.getMessage()
        );
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        Order order = findOrderById(id);

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
    public OrderResponse updateOrderStatus(
            Long id,
            String status) {

        Order order = findOrderById(id);

        order.setStatus(
                OrderStatus.valueOf(
                        status.toUpperCase()
                )
        );

        Order updatedOrder =
                orderRepository.save(order);

        return OrderMapper.toResponse(updatedOrder);
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                ORDER_NOT_FOUND + id
                        )
                );
    }
}
