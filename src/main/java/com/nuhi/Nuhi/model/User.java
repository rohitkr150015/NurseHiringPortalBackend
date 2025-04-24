package com.nuhi.Nuhi.model;


import com.nuhi.Nuhi.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Builder
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Role role; //  CLIENT,NURSE,ADMIN


    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable=false)
    private String password;


    @Column(nullable = false)
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;

    @Column(nullable =false)
    @Size(min=2 , max = 20 , message = "Last name must be 2-20 characters")
    private String lastName;

    @Pattern(regexp = "^\\+?[0-9\\s-]{10,}$", message = "Invalid phone number")
    private String phone;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;  // For soft deletes


}
