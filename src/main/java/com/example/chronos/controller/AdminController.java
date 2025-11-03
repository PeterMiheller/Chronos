package com.example.chronos.controller;

import com.example.chronos.model.Admin;
import com.example.chronos.service.AdminService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) { this.service = service; }

    @GetMapping
    public List<Admin> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Admin getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public Admin create(@RequestBody Admin admin) { return service.save(admin); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }
}
