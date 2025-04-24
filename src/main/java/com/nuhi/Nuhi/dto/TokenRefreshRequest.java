package com.nuhi.Nuhi.dto;

import jakarta.validation.constraints.NotBlank;

// TokenRefreshRequest.java
public record TokenRefreshRequest(
        @NotBlank String refreshToken
) {}
