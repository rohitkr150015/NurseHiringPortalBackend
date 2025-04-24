package com.nuhi.Nuhi.dto;

import com.nuhi.Nuhi.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// RegisterRequest.java
public record RegisterRequest(
        @NotBlank @Size(min = 2, max = 50) String firstName,
        @NotBlank @Size(min = 2, max = 50) String lastName,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password,
        @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$") String phone,
        Role role
) {}
