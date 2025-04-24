package com.nuhi.Nuhi.dto;

import com.nuhi.Nuhi.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 6) String password
) {}


