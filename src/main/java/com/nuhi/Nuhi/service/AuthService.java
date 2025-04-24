package com.nuhi.Nuhi.service;

import com.nuhi.Nuhi.dto.AuthRequest;
import com.nuhi.Nuhi.dto.AuthResponse;
import com.nuhi.Nuhi.dto.RegisterRequest;
import com.nuhi.Nuhi.dto.TokenRefreshRequest;
import com.nuhi.Nuhi.enums.Role;
import com.nuhi.Nuhi.exception.EmailAlreadyExistsException;
import com.nuhi.Nuhi.exception.InvalidCredentialsException;
import com.nuhi.Nuhi.exception.TokenRefreshException;
import com.nuhi.Nuhi.exception.UserNotFoundException;
import com.nuhi.Nuhi.model.User;
import com.nuhi.Nuhi.repository.UserRepository;
import com.nuhi.Nuhi.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;


    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())){
            throw new EmailAlreadyExistsException("Email already in use");
        }
        if( request.role() == null){
            throw new IllegalArgumentException("Role is required");
        }

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .role(request.role())  // Default role
                .build();;

                userRepository.save(user);

                return generateAuthResponse(user);
    }
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

            return generateAuthResponse(user);
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }


    public AuthResponse refreshToken(TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();
        String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

        UserDetails userDetails= userDetailsService.loadUserByUsername(username);

        if (!jwtTokenUtil.validateToken(refreshToken, userDetails )) {
            throw new TokenRefreshException(refreshToken, "Refresh token is invalid");
        }


        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return generateAuthResponse(user);
    }

    private AuthResponse generateAuthResponse(User user) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String accessToken = jwtTokenUtil.generateToken(userDetails);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }




}
