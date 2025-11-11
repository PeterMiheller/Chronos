package com.example.chronos.service;

import com.example.chronos.model.SuperAdmin;
import com.example.chronos.repository.SuperAdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SuperAdminService {
    private final SuperAdminRepository superAdminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SuperAdminService(SuperAdminRepository superAdminRepository) {
        this.superAdminRepository = superAdminRepository;
    }

    public SuperAdmin save(SuperAdmin superAdmin) {
        return superAdminRepository.save(superAdmin);
    }

    public List<SuperAdmin> findAll() {
        return superAdminRepository.findAll();
    }

    public SuperAdmin findById(int id) {
        return superAdminRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        superAdminRepository.deleteById(id);
    }

    public SuperAdmin findByEmail(String email) {
        return superAdminRepository.findByEmail(email);
    }

    public SuperAdmin createSuperAdmin(String name, String email, String rawPassword) {
        if (superAdminRepository.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        SuperAdmin superAdmin = new SuperAdmin(email, encodedPassword, name);
        return superAdminRepository.save(superAdmin);
    }

    public boolean validatePassword(String email, String rawPassword) {
        SuperAdmin superAdmin = superAdminRepository.findByEmail(email);
        if (superAdmin == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, superAdmin.getPassword());
    }
}
