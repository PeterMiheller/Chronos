package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.Admin;
import com.example.chronos.service.AdminService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) { this.service = service; }

    @GetMapping
    public List<Admin> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Admin getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public Admin create(@RequestBody Admin admin) { return service.save(admin); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            service.registerAdmin(request.getName(), request.getEmail(), request.getPassword());
            return ResponseEntity.ok("Admin registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = service.loginAdmin(request.getEmail(), request.getPassword());
        return success ?
                ResponseEntity.ok("Admin login successful") :
                ResponseEntity.status(401).body("Invalid credentials");
    }
}
