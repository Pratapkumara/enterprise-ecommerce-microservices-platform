package com.pratap.enterprise.paymentservice.repository;

import com.pratap.enterprise.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
