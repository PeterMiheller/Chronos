package com.example.chronos.controller;

import com.example.chronos.model.Employee;
import com.example.chronos.service.EmployeeService;
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
    public Employee create(@RequestBody Employee employee) { return service.save(employee); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) { service.deleteById(id); }
}
