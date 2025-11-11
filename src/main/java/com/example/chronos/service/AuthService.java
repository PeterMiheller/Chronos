package com.example.chronos.service;

import com.example.chronos.DTO.AuthResponse;
import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.model.User;
import com.example.chronos.repository.EmployeeRepository;
import com.example.chronos.repository.AdminRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(EmployeeRepository employeeRepository, AdminRepository adminRepository,
                       JwtService jwtService, AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Try to find employee first, then admin
        User user = employeeRepository.findByEmail(request.getEmail())
                .map(employee -> (User) employee)
                .orElseGet(() -> adminRepository.findByEmail(request.getEmail())
                        .orElseThrow(() -> new RuntimeException("User not found")));

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole());
        extraClaims.put("userId", user.getId());

        String jwtToken = jwtService.generateToken(extraClaims, (UserDetails) user);

        return new AuthResponse(
                jwtToken,
                user.getEmail(),
                user.getName(),
                user.getRole().toString()
        );
    }
}
