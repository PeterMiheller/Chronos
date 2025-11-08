package com.example.chronos.service;

import com.example.chronos.model.SuperAdmin;
import com.example.chronos.repository.SuperAdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuperAdminService {
    private final SuperAdminRepository repository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminService(SuperAdminRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public SuperAdmin save(SuperAdmin superAdmin) { return repository.save(superAdmin); }
    public List<SuperAdmin> findAll() { return repository.findAll(); }
    public SuperAdmin findById(int id) { return repository.findById(id).orElse(null); }
}
