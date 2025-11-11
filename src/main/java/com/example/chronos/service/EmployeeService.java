package com.example.chronos.service;

import com.example.chronos.model.Admin;
import com.example.chronos.model.Company;
import com.example.chronos.model.Employee;
import com.example.chronos.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        employeeRepository.deleteById(id);
    }

    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email).orElse(null);
    }

    public Employee createEmployee(String name, String email, String rawPassword, Admin admin, Company company, int vacationDaysTotal, float expectedWorkload) {
        if (employeeRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);
        Employee employee = new Employee(email, encodedPassword, name, admin, company, vacationDaysTotal, vacationDaysTotal, expectedWorkload);
        return employeeRepository.save(employee);
    }

    public boolean validatePassword(String email, String rawPassword) {
        Employee employee = employeeRepository.findByEmail(email).orElse(null);
        if (employee == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, employee.getPassword());
    }

    public List<Employee> findByAdminId(int adminId) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getAdmin().getId() == adminId)
                .toList();
    }

    public List<Employee> findByCompanyId(int companyId) {
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getCompany().getId() == companyId)
                .toList();
    }
}
