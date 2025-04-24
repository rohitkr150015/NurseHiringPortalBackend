package com.nuhi.Nuhi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ReviewDTO {

    private Long reviewId;
    private Long bookingId;
    private Long clientId;
    private Long nurseId;

    @Min(1) @Max(5)
    private int rating;

    @Size(max = 500)
    private String comment;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
}
