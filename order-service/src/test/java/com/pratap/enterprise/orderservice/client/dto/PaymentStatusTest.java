package com.pratap.enterprise.orderservice.client.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void enumShouldExposeEveryPaymentStatus() {
        PaymentStatus[] statuses =
                PaymentStatus.values();

        assertArrayEquals(
                new PaymentStatus[]{
                        PaymentStatus.SUCCESS,
                        PaymentStatus.FAILED,
                        PaymentStatus.PENDING
                },
                statuses
        );

        assertEquals(
                PaymentStatus.SUCCESS,
                PaymentStatus.valueOf("SUCCESS")
        );

        assertEquals(
                PaymentStatus.FAILED,
                PaymentStatus.valueOf("FAILED")
        );

        assertEquals(
                PaymentStatus.PENDING,
                PaymentStatus.valueOf("PENDING")
        );
    }
}
