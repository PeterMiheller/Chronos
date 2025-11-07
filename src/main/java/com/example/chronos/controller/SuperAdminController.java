package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.SuperAdmin;
import com.example.chronos.service.SuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/superadmins")
public class SuperAdminController {

    private final SuperAdminService service;

    public SuperAdminController(SuperAdminService service) { this.service = service; }

    @GetMapping
    public List<SuperAdmin> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public SuperAdmin getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public SuperAdmin create(@RequestBody SuperAdmin superAdmin) { return service.save(superAdmin); }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = service.loginSuperAdmin(request.getEmail(), request.getPassword());
        return success ?
                ResponseEntity.ok("Admin login successful") :
                ResponseEntity.status(401).body("Invalid credentials");
    }
}
