package com.nuhi.Nuhi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDTO(
        @NotNull Long bookingId,
        @NotBlank String paymentMethodId,
        boolean saveCard
) {
}
