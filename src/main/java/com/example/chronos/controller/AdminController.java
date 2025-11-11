package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.Admin;
import com.example.chronos.model.Company;
import com.example.chronos.service.AdminService;
import com.example.chronos.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final CompanyService companyService;

    public AdminController(AdminService adminService, CompanyService companyService) {
        this.adminService = adminService;
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Admin>> getAll() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getById(@PathVariable int id) {
        Admin admin = adminService.findById(id);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Admin> create(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.save(admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        adminService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Admin>> getByCompanyId(@PathVariable int companyId) {
        return ResponseEntity.ok(adminService.findByCompanyId(companyId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.getUserType().equalsIgnoreCase("ADMIN")) {
                return ResponseEntity.badRequest().body("User type must be ADMIN");
            }
            // For now, we need to know which company this admin belongs to
            // This should ideally be passed in the request
            Admin admin = adminService.createAdmin(request.getName(), request.getEmail(), request.getPassword(), null, 20);
            return ResponseEntity.ok(admin);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = adminService.validatePassword(request.getEmail(), request.getPassword());
        if (success) {
            Admin admin = adminService.findByEmail(request.getEmail());
            return ResponseEntity.ok(admin);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
