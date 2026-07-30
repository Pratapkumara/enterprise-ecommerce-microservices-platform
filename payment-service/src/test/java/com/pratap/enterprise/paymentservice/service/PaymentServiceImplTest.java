package com.pratap.enterprise.paymentservice.service;

import com.pratap.enterprise.paymentservice.dto.PaymentRequest;
import com.pratap.enterprise.paymentservice.dto.PaymentResponse;
import com.pratap.enterprise.paymentservice.entity.Payment;
import com.pratap.enterprise.paymentservice.entity.PaymentMethod;
import com.pratap.enterprise.paymentservice.entity.PaymentStatus;
import com.pratap.enterprise.paymentservice.mapper.PaymentMapper;
import com.pratap.enterprise.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private PaymentRequest request;
    private Payment payment;
    private PaymentResponse response;

    @BeforeEach
    void setUp() {
        request = PaymentRequest.builder()
                .orderId(1001L)
                .userId(101L)
                .amount(new BigDecimal("75000.00"))
                .paymentMethod(PaymentMethod.UPI)
                .build();

        payment = Payment.builder()
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
                .transactionId("TXN-12345")
                .paymentTime(LocalDateTime.now())
                .build();
    }

    @Test
    void processPaymentShouldCompleteAndReturnResponse() {
        when(paymentMapper.toEntity(request)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenAnswer(invocation -> {
            Payment saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        when(paymentMapper.toResponse(payment)).thenReturn(response);

        PaymentResponse result =
                paymentService.processPayment(request);

        assertSame(response, result);
        assertEquals(PaymentStatus.SUCCESS, payment.getPaymentStatus());
        assertNotNull(payment.getPaymentTime());
        assertNotNull(payment.getTransactionId());
        assertTrue(payment.getTransactionId().startsWith("TXN-"));

        verify(paymentMapper).toEntity(request);
        verify(paymentRepository).save(payment);
        verify(paymentMapper).toResponse(payment);
    }

    @Test
    void getPaymentByIdShouldReturnPayment() {
        when(paymentRepository.findById(1L))
                .thenReturn(Optional.of(payment));
        when(paymentMapper.toResponse(payment))
                .thenReturn(response);

        PaymentResponse result =
                paymentService.getPaymentById(1L);

        assertSame(response, result);
        verify(paymentRepository).findById(1L);
        verify(paymentMapper).toResponse(payment);
    }

    @Test
    void getPaymentByIdShouldThrowWhenMissing() {
        when(paymentRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> paymentService.getPaymentById(99L)
        );

        assertEquals("Payment not found", exception.getMessage());
        verify(paymentMapper, never()).toResponse(any());
    }

    @Test
    void getAllPaymentsShouldReturnMappedPayments() {
        Payment secondPayment = Payment.builder()
                .id(2L)
                .orderId(1002L)
                .userId(102L)
                .amount(new BigDecimal("1500.00"))
                .paymentMethod(PaymentMethod.CARD)
                .paymentStatus(PaymentStatus.SUCCESS)
                .transactionId("TXN-67890")
                .paymentTime(LocalDateTime.now())
                .build();

        PaymentResponse secondResponse =
                PaymentResponse.builder()
                        .id(2L)
                        .orderId(1002L)
                        .userId(102L)
                        .amount(new BigDecimal("1500.00"))
                        .paymentMethod(PaymentMethod.CARD)
                        .paymentStatus(PaymentStatus.SUCCESS)
                        .transactionId("TXN-67890")
                        .paymentTime(LocalDateTime.now())
                        .build();

        when(paymentRepository.findAll())
                .thenReturn(List.of(payment, secondPayment));
        when(paymentMapper.toResponse(payment))
                .thenReturn(response);
        when(paymentMapper.toResponse(secondPayment))
                .thenReturn(secondResponse);

        List<PaymentResponse> results =
                paymentService.getAllPayments();

        assertEquals(2, results.size());
        assertSame(response, results.get(0));
        assertSame(secondResponse, results.get(1));

        verify(paymentRepository).findAll();
        verify(paymentMapper).toResponse(payment);
        verify(paymentMapper).toResponse(secondPayment);
    }

    @Test
    void getAllPaymentsShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(List.of());

        List<PaymentResponse> results =
                paymentService.getAllPayments();

        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(paymentMapper, never()).toResponse(any());
    }
}
