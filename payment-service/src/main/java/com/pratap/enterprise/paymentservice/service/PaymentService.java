package com.pratap.enterprise.paymentservice.service;

import com.pratap.enterprise.paymentservice.dto.PaymentRequest;
import com.pratap.enterprise.paymentservice.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse processPayment(PaymentRequest request);

    PaymentResponse getPaymentById(Long id);

    List<PaymentResponse> getAllPayments();
}
