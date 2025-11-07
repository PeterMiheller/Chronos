package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminService {
    private final AdminRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public AdminService(AdminRepository repository) { this.repository = repository; }

    public Admin save(Admin admin) { return repository.save(admin); }
    public List<Admin> findAll() { return repository.findAll(); }
    public Admin findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }

    public Admin registerAdmin(String name, String email, String rawPassword) {
        if (repository.findByEmail(email) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        Admin user = new Admin();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(encodedPassword);

        return repository.save(user);
    }

    public boolean loginAdmin(String email, String rawPassword) {
        Admin user = repository.findByEmail(email);
        if (user == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}

