package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.SuperAdmin;
import com.example.chronos.service.SuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmins")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    public SuperAdminController(SuperAdminService superAdminService) {
        this.superAdminService = superAdminService;
    }

    @GetMapping
    public ResponseEntity<List<SuperAdmin>> getAll() {
        return ResponseEntity.ok(superAdminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuperAdmin> getById(@PathVariable int id) {
        SuperAdmin superAdmin = superAdminService.findById(id);
        return superAdmin != null ? ResponseEntity.ok(superAdmin) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<SuperAdmin> create(@RequestBody SuperAdmin superAdmin) {
        return ResponseEntity.ok(superAdminService.save(superAdmin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        superAdminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.getUserType().equalsIgnoreCase("SUPERADMIN")) {
                return ResponseEntity.badRequest().body("User type must be SUPERADMIN");
            }
            SuperAdmin superAdmin = superAdminService.createSuperAdmin(request.getName(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok(superAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = superAdminService.validatePassword(request.getEmail(), request.getPassword());
        if (success) {
            SuperAdmin superAdmin = superAdminService.findByEmail(request.getEmail());
            return ResponseEntity.ok(superAdmin);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
