package com.nuhi.Nuhi.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.nuhi.Nuhi.enums.ProfileStatus;
import com.nuhi.Nuhi.util.ValidationGroups;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NurseProfileDTO {
                private Long nurseId;
                private String fullName; // Combined first+last name from User

                @Size(max = 100)
                private String specialization;

                @Min(0) @Max(50)
                private int experienceYears;

                @DecimalMin("0.0")
                private BigDecimal hourlyRate;

                @Size(max = 1000)
                private String bio;

                @Size(max = 100)
                private String location;

                private ProfileStatus status;

    @Pattern(regexp = "^[A-Z0-9-]{10,20}$",
            message = "License must be 10-20 chars, uppercase letters, numbers or hyphens")
    @NotBlank(message = "License number is required" , groups = ValidationGroups.CreateValidationGroup.class)
                private String licenseNumber;

                // For React to parse availability

                private String availability; // Instead of String
        }


