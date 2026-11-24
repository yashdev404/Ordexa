package com.ordexa.backend.service;

import com.ordexa.backend.dto.AuthDto;
import com.ordexa.backend.entity.UserEntity;
import com.ordexa.backend.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthDto.JwtResponse login(AuthDto.LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Match password (or bypass for initial seed demo)
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash()) && !"••••••••".equals(request.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        String token = "mock-jwt-token-for-" + user.getId();
        return new AuthDto.JwtResponse(
                token,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCompanyId(),
                Collections.emptyList()
        );
    }
}
