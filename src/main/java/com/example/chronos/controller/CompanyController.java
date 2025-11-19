package com.example.chronos.controller;

import com.example.chronos.DTO.CompanyWithAdminsResponse;
import com.example.chronos.DTO.CreateCompanyRequest;
import com.example.chronos.model.Company;
import com.example.chronos.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/withAdmins")
    public ResponseEntity<List<CompanyWithAdminsResponse>> getAllCompaniesWithAdmins() {
        return ResponseEntity.ok(companyService.getAllCompaniesWithAdmins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable int id) {
        Company company = companyService.findById(id);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company savedCompany = companyService.save(company);
        return ResponseEntity.ok(savedCompany);
    }

    @PostMapping("/superadmin")
    public ResponseEntity<Company> createCompanybySuperAdmin(@RequestBody CreateCompanyRequest request) {
        Company savedCompany = companyService.createCompany(request.getName(), request.getAddress());
        return ResponseEntity.ok(savedCompany);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Company> updateCompany(@PathVariable int id, @RequestBody Company company) {
        Company existingCompany = companyService.findById(id);
        if (existingCompany == null) {
            return ResponseEntity.notFound().build();
        }
        company.setId(id);
        return ResponseEntity.ok(companyService.save(company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable int id) {
        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}