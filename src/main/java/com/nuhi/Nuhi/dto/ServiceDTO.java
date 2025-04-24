package com.nuhi.Nuhi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceDTO {

    @Size(min = 3, max = 100, message = "Name must be 3-100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal basePrice;
    }

