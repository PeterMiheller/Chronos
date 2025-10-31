package com.example.chronos.controller;

import com.example.chronos.model.Company;
import com.example.chronos.service.CompanyService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) { this.service = service; }

    @GetMapping
    public List<Company> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Company getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public Company create(@RequestBody Company company) { return service.save(company); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }
}
