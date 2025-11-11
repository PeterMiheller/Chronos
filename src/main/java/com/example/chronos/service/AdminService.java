package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.model.Company;
import com.example.chronos.repository.AdminRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public Admin findById(int id) {
        return adminRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        adminRepository.deleteById(id);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public Admin createAdmin(String name, String email, String rawPassword, Company company, int vacationDaysTotal) {
        if (adminRepository.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        Admin admin = new Admin(email, encodedPassword, name, company, vacationDaysTotal, vacationDaysTotal);
        return adminRepository.save(admin);
    }

    public boolean validatePassword(String email, String rawPassword) {
        Admin admin = adminRepository.findByEmail(email);
        if (admin == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, admin.getPassword());
    }

    public List<Admin> findByCompanyId(int companyId) {
        return adminRepository.findAll().stream()
                .filter(admin -> admin.getCompany().getId() == companyId)
                .toList();
    }
}

