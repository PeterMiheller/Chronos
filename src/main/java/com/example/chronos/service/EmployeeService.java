package com.example.chronos.service;

import com.example.chronos.model.Employee;
import com.example.chronos.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) { this.repository = repository; }

    public Employee save(Employee employee) { return repository.save(employee); }
    public List<Employee> findAll() { return repository.findAll(); }
    public Employee findById(int id) { return repository.findById(id).orElse(null); }
    public void deleteById(int id) { repository.deleteById(id); }
}
