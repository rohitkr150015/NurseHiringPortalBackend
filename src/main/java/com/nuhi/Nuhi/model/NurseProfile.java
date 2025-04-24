package com.nuhi.Nuhi.model;

import com.nuhi.Nuhi.enums.ProfileStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "nurse_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class NurseProfile {

    @Id
    private Long nurseId;

    @Version
    private Long version;

    @OneToOne
    @MapsId
    @JoinColumn(name = "nurse_id")
    private User user;

    @Column(name = "license_number", unique = true, nullable = false)
    @Pattern(regexp = "^[A-Z0-9-]{10,20}$", message = "License must be 10-20 chars: A-Z, 0-9, hyphens")
    private String licenseNumber;


    @Column(length = 100)
    @Size(max = 100)
    private String specialization;

    @Column(name = "experience_years")
    @Min(0)
    @Max(50)
    private int experienceYears;

    @Column(name = "hourly_rate", precision = 10, scale = 2)
    @DecimalMin("0.0")
    private BigDecimal hourly_rate;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000)
    private String bio;

    @Column(columnDefinition = "jsonb")
    private String availability;

    private String location;

    @Enumerated(EnumType.STRING)
    private ProfileStatus status = ProfileStatus.PENDING;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // Add this to prevent null values
    @PrePersist
    protected void onCreate() {
        if (this.status == null) {
            this.status = ProfileStatus.PENDING;
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

    }
}
