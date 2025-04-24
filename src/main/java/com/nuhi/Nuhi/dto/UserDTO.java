package com.nuhi.Nuhi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
private Long id;

    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be 2-50 characters")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Invalid phone number")
    private String phone;
    private String role;
}
