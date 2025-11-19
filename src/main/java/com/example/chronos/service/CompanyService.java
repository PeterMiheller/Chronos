package com.example.chronos.service;

import com.example.chronos.model.Company;
import com.example.chronos.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        companyRepository.deleteById(id);
    }

    public Company createCompany(String name, String address) {
        Company company = new Company(name, address);
        return companyRepository.save(company);
    }

    public Company getDefaultCompany() {
        Company defaultCompany = new Company("Default Company", "Default Address");
        defaultCompany.setId(1);
        return defaultCompany;
    }
}