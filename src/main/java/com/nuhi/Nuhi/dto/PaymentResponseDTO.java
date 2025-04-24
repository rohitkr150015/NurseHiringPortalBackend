package com.nuhi.Nuhi.dto;

import com.nuhi.Nuhi.enums.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponseDTO(
        Long id,
        Long bookingId,
        BigDecimal amount,
        PaymentStatus status,
        String transactionId,
        String clientSecret
) {
}
