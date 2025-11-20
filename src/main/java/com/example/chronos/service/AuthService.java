package com.example.chronos.service;

import com.example.chronos.DTO.AuthResponse;
import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.model.User;
import com.example.chronos.repository.UserRepository;
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

    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(LoginRequest request) {

        // 1. Authentication of the credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("role", user.getUserType().name());
        claims.put("administratorId", user.getAdministratorId());


        String jwtToken = jwtService.generateToken(claims, user);


        Integer companyId = user.getCompany() != null ? user.getCompany().getId() : null;

        return new AuthResponse(
                user.getId(),
                user.getAdministratorId(),
                jwtToken,
                user.getEmail(),
                user.getName(),
                user.getUserType().name(),
                companyId
        );
    }
}
