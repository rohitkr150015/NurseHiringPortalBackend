package com.nuhi.Nuhi.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record BookingResponseDTO(
        @NotNull Long nurseId,
        @NotNull Long serviceId,
        @Future LocalDateTime startTime,
        @Future LocalDateTime endTime
) {
}
