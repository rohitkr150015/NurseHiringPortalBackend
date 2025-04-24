package com.nuhi.Nuhi.dto;

import com.nuhi.Nuhi.enums.Role;
import lombok.Builder;

// AuthResponse.java

@Builder
public record AuthResponse(
        String accessToken,
        String email,
        Role role
) {}
