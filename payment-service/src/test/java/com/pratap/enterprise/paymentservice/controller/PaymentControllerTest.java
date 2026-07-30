package com.pratap.enterprise.paymentservice.controller;

import com.pratap.enterprise.paymentservice.dto.PaymentRequest;
import com.pratap.enterprise.paymentservice.dto.PaymentResponse;
import com.pratap.enterprise.paymentservice.entity.PaymentMethod;
import com.pratap.enterprise.paymentservice.entity.PaymentStatus;
import com.pratap.enterprise.paymentservice.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController controller;

    private PaymentRequest request;
    private PaymentResponse response;

    @BeforeEach
    void setUp() {
        request = PaymentRequest.builder()
                .orderId(1001L)
                .userId(101L)
                .amount(new BigDecimal("75000.00"))
                .paymentMethod(PaymentMethod.UPI)
                .build();

        response = PaymentResponse.builder()
                .id(1L)
                .orderId(1001L)
                .userId(101L)
                .amount(new BigDecimal("75000.00"))
                .paymentMethod(PaymentMethod.UPI)
                .paymentStatus(PaymentStatus.SUCCESS)
                .build();
    }

    @Test
    void processPaymentShouldReturnServiceResponse() {
        when(paymentService.processPayment(request))
                .thenReturn(response);

        PaymentResponse result =
                controller.processPayment(request);

        assertSame(response, result);
        verify(paymentService).processPayment(request);
    }

    @Test
    void getPaymentByIdShouldReturnServiceResponse() {
        when(paymentService.getPaymentById(1L))
                .thenReturn(response);

        PaymentResponse result =
                controller.getPaymentById(1L);

        assertSame(response, result);
        verify(paymentService).getPaymentById(1L);
    }

    @Test
    void getAllPaymentsShouldReturnServiceList() {
        when(paymentService.getAllPayments())
                .thenReturn(List.of(response));

        List<PaymentResponse> results =
                controller.getAllPayments();

        assertEquals(1, results.size());
        assertSame(response, results.get(0));
        verify(paymentService).getAllPayments();
    }
}
