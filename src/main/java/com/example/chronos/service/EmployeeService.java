package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.model.Employee;
import com.example.chronos.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public EmployeeService(EmployeeRepository repository) { this.repository = repository; }

    public Employee save(Employee employee) { return repository.save(employee); }
    public List<Employee> findAll() { return repository.findAll(); }
    public Employee findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
// this is not needed for employees, they will be created by admin, this code could be used later tho
//    public Employee registerEmployee(String name, String email, String rawPassword) {
//        if (repository.findByEmail(email) != null) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
//        }
//
//        String encodedPassword = passwordEncoder.encode(rawPassword);
//        Employee user = new Employee();
//        user.setName(name);
//        user.setEmail(email);
//        user.setPassword(encodedPassword);
//
//        return repository.save(user);
//    }

    public boolean loginEmployee(String email, String rawPassword) {
        // this is a mock implementation, until we have a db
        // Employee user = repository.findByEmail(email);
//        if (user == null) {
//            return false;
//        }
        String mockEmail = "test@example.com";
        String mockPassword = "123456";

        if (!email.equals(mockEmail)) {
            return false;
        }
        return email.equals(mockEmail) && rawPassword.equals(mockPassword);
    }
}
