package com.pratap.enterprise.orderservice.service;

import com.pratap.enterprise.orderservice.client.NotificationClient;
import com.pratap.enterprise.orderservice.client.PaymentClient;
import com.pratap.enterprise.orderservice.client.ProductClient;
import com.pratap.enterprise.orderservice.client.dto.NotificationRequest;
import com.pratap.enterprise.orderservice.client.dto.PaymentMethod;
import com.pratap.enterprise.orderservice.client.dto.PaymentRequest;
import com.pratap.enterprise.orderservice.client.dto.PaymentResponse;
import com.pratap.enterprise.orderservice.client.dto.ProductResponse;
import com.pratap.enterprise.orderservice.dto.OrderItemRequest;
import com.pratap.enterprise.orderservice.dto.OrderRequest;
import com.pratap.enterprise.orderservice.dto.OrderResponse;
import com.pratap.enterprise.orderservice.entity.Order;
import com.pratap.enterprise.orderservice.entity.OrderItem;
import com.pratap.enterprise.orderservice.entity.OrderStatus;
import com.pratap.enterprise.orderservice.exception.OrderNotFoundException;
import com.pratap.enterprise.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private PaymentClient paymentClient;

    @Mock
    private NotificationClient notificationClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;

    @BeforeEach
    void setUp() {

        order = Order.builder()
                .id(1L)
                .userId(10L)
                .totalAmount(new BigDecimal("350.00"))
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .build();

        OrderItem firstItem = OrderItem.builder()
                .id(1L)
                .productId(101L)
                .quantity(2)
                .price(new BigDecimal("100.00"))
                .order(order)
                .build();

        OrderItem secondItem = OrderItem.builder()
                .id(2L)
                .productId(102L)
                .quantity(3)
                .price(new BigDecimal("50.00"))
                .order(order)
                .build();

        order.setItems(List.of(firstItem, secondItem));
    }

    @Test
    void createOrderShouldCreateOrderAndCallExternalServices() {

        OrderRequest request = OrderRequest.builder()
                .userId(10L)
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(101L)
                                .quantity(2)
                                .build(),
                        OrderItemRequest.builder()
                                .productId(102L)
                                .quantity(3)
                                .build()
                ))
                .build();

        ProductResponse firstProduct = ProductResponse.builder()
                .id(101L)
                .name("Laptop")
                .price(new BigDecimal("100.00"))
                .build();

        ProductResponse secondProduct = ProductResponse.builder()
                .id(102L)
                .name("Mouse")
                .price(new BigDecimal("50.00"))
                .build();

        when(productClient.getProductById(101L))
                .thenReturn(firstProduct);

        when(productClient.getProductById(102L))
                .thenReturn(secondProduct);

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order savedOrder = invocation.getArgument(0);
                    savedOrder.setId(1L);
                    savedOrder.setStatus(OrderStatus.CREATED);
                    savedOrder.setCreatedAt(LocalDateTime.now());

                    for (int index = 0;
                         index < savedOrder.getItems().size();
                         index++) {

                        savedOrder.getItems()
                                .get(index)
                                .setId((long) index + 1);
                    }

                    return savedOrder;
                });

        when(paymentClient.processPayment(any(PaymentRequest.class)))
                .thenReturn(PaymentResponse.builder()
                        .id(1L)
                        .orderId(1L)
                        .userId(10L)
                        .amount(new BigDecimal("350.00"))
                        .paymentMethod(PaymentMethod.UPI)
                        .build());

        OrderResponse response = orderService.createOrder(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(2, response.getItems().size());
        assertEquals(
                0,
                new BigDecimal("350.00")
                        .compareTo(response.getTotalAmount())
        );

        verify(productClient).getProductById(101L);
        verify(productClient).getProductById(102L);
        verify(orderRepository).save(any(Order.class));

        ArgumentCaptor<PaymentRequest> paymentCaptor =
                ArgumentCaptor.forClass(PaymentRequest.class);

        verify(paymentClient).processPayment(paymentCaptor.capture());

        PaymentRequest paymentRequest = paymentCaptor.getValue();

        assertEquals(1L, paymentRequest.getOrderId());
        assertEquals(10L, paymentRequest.getUserId());
        assertEquals(PaymentMethod.UPI,
                paymentRequest.getPaymentMethod());
        assertEquals(
                0,
                new BigDecimal("350.00")
                        .compareTo(paymentRequest.getAmount())
        );

        ArgumentCaptor<NotificationRequest> notificationCaptor =
                ArgumentCaptor.forClass(NotificationRequest.class);

        verify(notificationClient)
                .sendNotification(notificationCaptor.capture());

        NotificationRequest notificationRequest =
                notificationCaptor.getValue();

        assertEquals(10L, notificationRequest.getUserId());
        assertEquals("test@gmail.com",
                notificationRequest.getEmail());
        assertEquals(
                "Your order has been created successfully",
                notificationRequest.getMessage()
        );
        assertEquals("ORDER", notificationRequest.getType());
    }

    @Test
    void createOrderShouldPropagateProductClientFailure() {

        OrderRequest request = OrderRequest.builder()
                .userId(10L)
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(999L)
                                .quantity(1)
                                .build()
                ))
                .build();

        when(productClient.getProductById(999L))
                .thenThrow(new RuntimeException(
                        "Product service unavailable"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> orderService.createOrder(request)
        );

        assertEquals(
                "Product service unavailable",
                exception.getMessage()
        );

        verify(productClient).getProductById(999L);
        verifyNoInteractions(
                orderRepository,
                paymentClient,
                notificationClient
        );
    }

    @Test
    void sendNotificationShouldCallNotificationClient() {

        orderService.sendNotification(order);

        ArgumentCaptor<NotificationRequest> captor =
                ArgumentCaptor.forClass(NotificationRequest.class);

        verify(notificationClient)
                .sendNotification(captor.capture());

        assertEquals(10L, captor.getValue().getUserId());
        assertEquals("ORDER", captor.getValue().getType());
    }

    @Test
    void notificationFallbackShouldNotThrowException() {

        assertDoesNotThrow(() ->
                orderService.notificationFallback(
                        order,
                        new RuntimeException(
                                "Notification service unavailable")
                )
        );
    }

    @Test
    void getOrderByIdShouldReturnOrderWhenFound() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        OrderResponse response =
                orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(10L, response.getUserId());
        assertEquals(OrderStatus.CREATED, response.getStatus());
        assertEquals(2, response.getItems().size());

        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderByIdShouldThrowExceptionWhenNotFound() {

        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.getOrderById(99L)
        );

        assertEquals(
                "Order not found with id: 99",
                exception.getMessage()
        );

        verify(orderRepository).findById(99L);
    }

    @Test
    void getAllOrdersShouldReturnOrderList() {

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        List<OrderResponse> responses =
                orderService.getAllOrders();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals(2, responses.get(0).getItems().size());

        verify(orderRepository).findAll();
    }

    @Test
    void getAllOrdersShouldReturnEmptyList() {

        when(orderRepository.findAll())
                .thenReturn(List.of());

        List<OrderResponse> responses =
                orderService.getAllOrders();

        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(orderRepository).findAll();
    }

    @Test
    void updateOrderStatusShouldUpdateAndReturnOrder() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        when(orderRepository.save(order))
                .thenReturn(order);

        OrderResponse response =
                orderService.updateOrderStatus(1L, "confirmed");

        assertNotNull(response);
        assertEquals(OrderStatus.CONFIRMED,
                response.getStatus());
        assertEquals(OrderStatus.CONFIRMED,
                order.getStatus());

        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrderStatusShouldThrowExceptionWhenNotFound() {

        when(orderRepository.findById(99L))
                .thenReturn(Optional.empty());

        OrderNotFoundException exception = assertThrows(
                OrderNotFoundException.class,
                () -> orderService.updateOrderStatus(
                        99L,
                        "confirmed"
                )
        );

        assertEquals(
                "Order not found with id: 99",
                exception.getMessage()
        );

        verify(orderRepository).findById(99L);
        verify(orderRepository, never())
                .save(any(Order.class));
    }

    @Test
    void updateOrderStatusShouldRejectInvalidStatus() {

        when(orderRepository.findById(1L))
                .thenReturn(Optional.of(order));

        assertThrows(
                IllegalArgumentException.class,
                () -> orderService.updateOrderStatus(
                        1L,
                        "invalid-status"
                )
        );

        verify(orderRepository).findById(1L);
        verify(orderRepository, never())
                .save(any(Order.class));
    }
}
