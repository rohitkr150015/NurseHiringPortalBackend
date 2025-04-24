package com.nuhi.Nuhi.controller;

import com.nuhi.Nuhi.dto.*;
import com.nuhi.Nuhi.security.JwtTokenUtil;
import com.nuhi.Nuhi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

public class AuthController {

private final JwtTokenUtil jwtTokenUtil;
private  final UserDetailsService userDetailsService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }
    @GetMapping("/api/auth/token-info")
    public ResponseEntity<?> getTokenInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        return ResponseEntity.ok(Map.of(
                "username", jwtTokenUtil.getUsernameFromToken(token),
                "roles", jwtTokenUtil.getRolesFromToken(token),
                "valid", jwtTokenUtil.validateToken(token,
                        userDetailsService.loadUserByUsername(
                                jwtTokenUtil.getUsernameFromToken(token)
                        )
                )
        ));
    }

}