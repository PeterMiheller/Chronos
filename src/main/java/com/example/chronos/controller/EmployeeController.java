package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.Employee;
import com.example.chronos.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) { this.service = service; }

    @GetMapping
    public List<Employee> getAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable int id) { return service.findById(id); }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return service.saveWithEncodedPassword(employee);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }
//we dont need a register endpoint for employees, they will be created by admin
//    @PostMapping("/register")
//    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
//        try {
//            service.registerEmployee(request.getName(), request.getEmail(), request.getPassword());
//            return ResponseEntity.ok("Employee registered successfully");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

}
