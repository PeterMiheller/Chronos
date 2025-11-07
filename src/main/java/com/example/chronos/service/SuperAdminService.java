package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.model.SuperAdmin;
import com.example.chronos.repository.SuperAdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SuperAdminService {
    private final SuperAdminRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public SuperAdminService(SuperAdminRepository repository) { this.repository = repository; }

    public SuperAdmin save(SuperAdmin superAdmin) { return repository.save(superAdmin); }
    public List<SuperAdmin> findAll() { return repository.findAll(); }
    public SuperAdmin findById(int id) { return repository.findById(id).orElse(null); }


    public boolean loginSuperAdmin(String email, String rawPassword) {
        SuperAdmin user = repository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
