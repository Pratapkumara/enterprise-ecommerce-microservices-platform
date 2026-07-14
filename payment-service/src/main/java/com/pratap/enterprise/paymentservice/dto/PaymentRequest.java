package com.pratap.enterprise.paymentservice.dto;

import com.pratap.enterprise.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private Long userId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;
}
