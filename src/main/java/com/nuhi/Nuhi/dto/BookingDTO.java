package com.nuhi.Nuhi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuhi.Nuhi.enums.BookingStatus;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class BookingDTO {

        private Long bookingId;
        private Long clientId;
        private Long nurseId;
        private Long serviceId;

        @Future(message = "Start time must be in the future")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime startTime;

        @Future
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime endTime;

        @DecimalMin("0.0")
        private BigDecimal totalCost;

        private BookingStatus status;

        // For React display
        private String clientName;
        private String nurseName;
        private String serviceName;
    }

