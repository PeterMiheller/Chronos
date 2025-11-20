package com.example.chronos.controller;

import com.example.chronos.DTO.AuthResponse;
import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Auth endpoint is working!");
    }

    @GetMapping("/validate")
    public ResponseEntity<Map<String, Boolean>> validateToken(Authentication authentication) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("valid", authentication != null && authentication.isAuthenticated());
        return ResponseEntity.ok(response);
    }
}
