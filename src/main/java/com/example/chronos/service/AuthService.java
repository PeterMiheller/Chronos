package com.example.chronos.service;

import com.example.chronos.DTO.AuthResponse;
import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.model.User;
import com.example.chronos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public AuthService(UserRepository userRepository,
            JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Find user by email
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        extraClaims.put("userId", user.getId());

        String jwtToken = jwtService.generateToken(extraClaims, user);

        // Calculate expiration timestamp
        Long expiresAt = System.currentTimeMillis() + jwtExpiration;

        Integer companyId = user.getCompany() != null ? user.getCompany().getId() : null;

        return new AuthResponse(
                user.getId(),
                jwtToken,
                user.getEmail(),
                user.getName(),
                user.getRole().name(),
                companyId,
                expiresAt);
    }
}
