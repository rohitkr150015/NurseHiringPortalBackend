package com.nuhi.Nuhi.model;


import com.nuhi.Nuhi.enums.BookingStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Booking {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nurse_id")
    private User nurse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

@Column(name = "start_time")
    private LocalDateTime startTime;

@Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "total_cost", nullable = false, precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal totalCost;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


}
