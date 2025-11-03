package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.repository.AdminRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository repository;

    public AdminService(AdminRepository repository) { this.repository = repository; }

    public Admin save(Admin admin) { return repository.save(admin); }
    public List<Admin> findAll() { return repository.findAll(); }
    public Admin findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
}
