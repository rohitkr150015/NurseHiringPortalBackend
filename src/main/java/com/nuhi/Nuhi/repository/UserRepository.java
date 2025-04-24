package com.nuhi.Nuhi.repository;

import com.nuhi.Nuhi.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends JpaRepository<User ,Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(@NotBlank @Email String attr0);
}
