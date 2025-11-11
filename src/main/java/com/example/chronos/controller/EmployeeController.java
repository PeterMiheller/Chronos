package com.example.chronos.controller;

import com.example.chronos.DTO.LoginRequest;
import com.example.chronos.DTO.RegisterRequest;
import com.example.chronos.model.Employee;
import com.example.chronos.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getById(@PathVariable int id) {
        Employee employee = employeeService.findById(id);
        return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        employeeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<Employee>> getByAdminId(@PathVariable int adminId) {
        return ResponseEntity.ok(employeeService.findByAdminId(adminId));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Employee>> getByCompanyId(@PathVariable int companyId) {
        return ResponseEntity.ok(employeeService.findByCompanyId(companyId));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (!request.getUserType().equalsIgnoreCase("EMPLOYEE")) {
                return ResponseEntity.badRequest().body("User type must be EMPLOYEE");
            }
            // For now, we need admin and company info to create an employee
            // This should ideally be passed in the request
            Employee employee = employeeService.createEmployee(request.getName(), request.getEmail(), request.getPassword(), null, null, 20, 100.0f);
            return ResponseEntity.ok(employee);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        boolean success = employeeService.validatePassword(request.getEmail(), request.getPassword());
        if (success) {
            Employee employee = employeeService.findByEmail(request.getEmail());
            return ResponseEntity.ok(employee);
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
