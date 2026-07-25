package com.pratap.enterprise.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratap.enterprise.orderservice.dto.OrderItemRequest;
import com.pratap.enterprise.orderservice.dto.OrderItemResponse;
import com.pratap.enterprise.orderservice.dto.OrderRequest;
import com.pratap.enterprise.orderservice.dto.OrderResponse;
import com.pratap.enterprise.orderservice.entity.OrderStatus;
import com.pratap.enterprise.orderservice.exception.OrderNotFoundException;
import com.pratap.enterprise.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderRequest request;
    private OrderResponse response;

    @BeforeEach
    void setUp() {

        request = OrderRequest.builder()
                .userId(10L)
                .items(List.of(
                        OrderItemRequest.builder()
                                .productId(101L)
                                .quantity(2)
                                .build()
                ))
                .build();

        response = OrderResponse.builder()
                .id(1L)
                .userId(10L)
                .totalAmount(new BigDecimal("200.00"))
                .status(OrderStatus.CREATED)
                .createdAt(LocalDateTime.now())
                .items(List.of(
                        OrderItemResponse.builder()
                                .id(1L)
                                .productId(101L)
                                .quantity(2)
                                .price(new BigDecimal("100.00"))
                                .build()
                ))
                .build();
    }

    @Test
    void createOrderShouldReturn201() throws Exception {

        when(orderService.createOrder(any(OrderRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.totalAmount").value(200.00))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.items[0].productId").value(101))
                .andExpect(jsonPath("$.items[0].quantity").value(2));

        verify(orderService).createOrder(any(OrderRequest.class));
    }

    @Test
    void createOrderShouldReturn400WhenUserIdIsMissing()
            throws Exception {

        request.setUserId(null);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    void createOrderShouldReturn400ForInvalidItemQuantity()
            throws Exception {

        request.setItems(List.of(
                OrderItemRequest.builder()
                        .productId(101L)
                        .quantity(0)
                        .build()
        ));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }

    @Test
    void getOrderByIdShouldReturn200() throws Exception {

        when(orderService.getOrderById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/api/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(10))
                .andExpect(jsonPath("$.status").value("CREATED"));

        verify(orderService).getOrderById(1L);
    }

    @Test
    void getOrderByIdShouldReturn404WhenNotFound()
            throws Exception {

        when(orderService.getOrderById(99L))
                .thenThrow(new OrderNotFoundException(
                        "Order not found with id: 99"));

        mockMvc.perform(get("/api/orders/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Order not found with id: 99"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(orderService).getOrderById(99L);
    }

    @Test
    void getAllOrdersShouldReturn200() throws Exception {

        when(orderService.getAllOrders())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].status").value("CREATED"));

        verify(orderService).getAllOrders();
    }

    @Test
    void getAllOrdersShouldReturnEmptyList() throws Exception {

        when(orderService.getAllOrders())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(orderService).getAllOrders();
    }

    @Test
    void updateOrderStatusShouldReturn200() throws Exception {

        response.setStatus(OrderStatus.CONFIRMED);

        when(orderService.updateOrderStatus(1L, "confirmed"))
                .thenReturn(response);

        mockMvc.perform(put("/api/orders/{id}/status", 1L)
                        .param("status", "confirmed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderService)
                .updateOrderStatus(1L, "confirmed");
    }

    @Test
    void updateOrderStatusShouldReturn404WhenNotFound()
            throws Exception {

        when(orderService.updateOrderStatus(99L, "confirmed"))
                .thenThrow(new OrderNotFoundException(
                        "Order not found with id: 99"));

        mockMvc.perform(put("/api/orders/{id}/status", 99L)
                        .param("status", "confirmed"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Order not found with id: 99"))
                .andExpect(jsonPath("$.timestamp").exists());

        verify(orderService)
                .updateOrderStatus(99L, "confirmed");
    }

    @Test
    void updateOrderStatusShouldReturn400WhenStatusIsMissing()
            throws Exception {

        mockMvc.perform(put("/api/orders/{id}/status", 1L))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(orderService);
    }
}
