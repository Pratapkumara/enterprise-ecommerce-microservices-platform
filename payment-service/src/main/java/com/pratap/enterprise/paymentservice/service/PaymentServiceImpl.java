package com.pratap.enterprise.paymentservice.service;

import com.pratap.enterprise.paymentservice.dto.PaymentRequest;
import com.pratap.enterprise.paymentservice.dto.PaymentResponse;
import com.pratap.enterprise.paymentservice.entity.Payment;
import com.pratap.enterprise.paymentservice.entity.PaymentStatus;
import com.pratap.enterprise.paymentservice.mapper.PaymentMapper;
import com.pratap.enterprise.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    public PaymentResponse processPayment(PaymentRequest request) {

        Payment payment = paymentMapper.toEntity(request);

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId("TXN-" + System.currentTimeMillis());
        payment.setPaymentTime(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        return paymentMapper.toResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }
}
