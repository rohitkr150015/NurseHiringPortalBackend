package com.nuhi.Nuhi.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "booking_id" , nullable=false)
    private Booking booking;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "client_id" , nullable=false)
    private User client;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "nurse_id"  , nullable=false)
    private User nurse;

    @Min(value=1 , message = "Rating must be least 1")
    @Max(value=5 , message="Rating cannot exceed 5")
    private int rating;

    @Column(length = 500)
    @Size(max=500 , message = "Comment cannot exceed 500 characters")
    private String comment;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;


    private boolean isActive =true ; //For soft deletion
}
