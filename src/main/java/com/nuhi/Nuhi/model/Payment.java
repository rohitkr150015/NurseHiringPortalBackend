package com.nuhi.Nuhi.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nuhi.Nuhi.enums.PaymentMethod;
import com.nuhi.Nuhi.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable=false , unique=true)
    private Booking booking;

    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;


    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(name = "transaction_id", unique=true)
    private String transactionId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private LocalDateTime paidAt;


}
