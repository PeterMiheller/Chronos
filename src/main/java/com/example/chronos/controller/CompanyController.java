package com.example.chronos.controller;

import com.example.chronos.model.Company;
import com.example.chronos.model.SuperAdmin;
import com.example.chronos.service.CompanyService;
import com.example.chronos.service.SuperAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

    private final CompanyService companyService;
    private final SuperAdminService superAdminService;

    public CompanyController(CompanyService companyService, SuperAdminService superAdminService) {
        this.companyService = companyService;
        this.superAdminService = superAdminService;
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAll() {
        return ResponseEntity.ok(companyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getById(@PathVariable int id) {
        Company company = companyService.findById(id);
        return company != null ? ResponseEntity.ok(company) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Company> create(@RequestBody Company company) {
        return ResponseEntity.ok(companyService.save(company));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        companyService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/superadmin/{superAdminId}")
    public ResponseEntity<List<Company>> getBySuperAdminId(@PathVariable int superAdminId) {
        return ResponseEntity.ok(companyService.findBySuperAdminId(superAdminId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCompany(@RequestParam String name, @RequestParam String address, @RequestParam int superAdminId) {
        try {
            SuperAdmin superAdmin = superAdminService.findById(superAdminId);
            if (superAdmin == null) {
                return ResponseEntity.badRequest().body("SuperAdmin not found");
            }
            Company company = companyService.createCompany(name, address, superAdmin);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
