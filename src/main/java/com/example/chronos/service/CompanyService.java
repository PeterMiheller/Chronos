package com.example.chronos.service;

import com.example.chronos.model.Company;
import com.example.chronos.repository.CompanyRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository repository;

    public CompanyService(CompanyRepository repository) { this.repository = repository; }

    public Company save(Company company) { return repository.save(company); }
    public List<Company> findAll() { return repository.findAll(); }
    public Company findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
}
