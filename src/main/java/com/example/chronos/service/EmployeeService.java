package com.example.chronos.service;

import com.example.chronos.model.Employee;
import com.example.chronos.repository.EmployeeRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Employee save(Employee employee) {
        return repository.save(employee);
    }

    public Employee saveWithEncodedPassword(Employee employee) {
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        return repository.save(employee);
    }

    public List<Employee> findAll() { return repository.findAll(); }
    public Employee findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
}
