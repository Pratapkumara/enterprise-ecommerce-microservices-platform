package com.pratap.enterprise.orderservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void prePersistShouldInitializeTimestampAndDefaultStatus() {
        Order order = Order.builder()
                .userId(101L)
                .build();

        assertNull(order.getCreatedAt());
        assertNull(order.getStatus());

        order.prePersist();

        assertNotNull(order.getCreatedAt());
        assertEquals(OrderStatus.CREATED, order.getStatus());
    }

    @Test
    void prePersistShouldPreserveExistingStatus() {
        Order order = Order.builder()
                .userId(101L)
                .status(OrderStatus.CONFIRMED)
                .build();

        order.prePersist();

        assertNotNull(order.getCreatedAt());
        assertEquals(
                OrderStatus.CONFIRMED,
                order.getStatus()
        );
    }
}
