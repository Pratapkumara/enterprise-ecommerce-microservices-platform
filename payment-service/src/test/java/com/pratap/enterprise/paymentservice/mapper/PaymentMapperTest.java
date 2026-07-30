package com.pratap.enterprise.paymentservice.mapper;

import com.pratap.enterprise.paymentservice.dto.PaymentRequest;
import com.pratap.enterprise.paymentservice.dto.PaymentResponse;
import com.pratap.enterprise.paymentservice.entity.Payment;
import com.pratap.enterprise.paymentservice.entity.PaymentMethod;
import com.pratap.enterprise.paymentservice.entity.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    private PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapperImpl();
    }

    @Test
    void toEntityShouldMapEveryRequestField() {
        PaymentRequest request = PaymentRequest.builder()
                .orderId(1001L)
                .userId(101L)
                .amount(new BigDecimal("75000.00"))
                .paymentMethod(PaymentMethod.UPI)
                .build();

        Payment payment = mapper.toEntity(request);

        assertNotNull(payment);
        assertEquals(1001L, payment.getOrderId());
        assertEquals(101L, payment.getUserId());
        assertEquals(
                new BigDecimal("75000.00"),
                payment.getAmount()
        );
        assertEquals(
                PaymentMethod.UPI,
                payment.getPaymentMethod()
        );
    }

    @Test
    void toEntityShouldReturnNullForNullRequest() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    void toResponseShouldMapEveryPaymentField() {
        LocalDateTime paymentTime = LocalDateTime.now();

        Payment payment = Payment.builder()
                .id(1L)
                .orderId(1001L)
                .userId(101L)
                .amount(new BigDecimal("75000.00"))
                .paymentMethod(PaymentMethod.CARD)
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId("TXN-12345")
                .paymentTime(paymentTime)
                .build();

        PaymentResponse response = mapper.toResponse(payment);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(1001L, response.getOrderId());
        assertEquals(101L, response.getUserId());
        assertEquals(
                new BigDecimal("75000.00"),
                response.getAmount()
        );
        assertEquals(
                PaymentMethod.CARD,
                response.getPaymentMethod()
        );
        assertEquals(
                PaymentStatus.SUCCESS,
                response.getPaymentStatus()
        );
        assertEquals("TXN-12345", response.getTransactionId());
        assertEquals(paymentTime, response.getPaymentTime());
    }

    @Test
    void toResponseShouldReturnNullForNullPayment() {
        assertNull(mapper.toResponse(null));
    }
}
