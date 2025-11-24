package com.example.chronos.service;

import com.example.chronos.DTO.CreateAdminRequest;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    // Get all employees
    public List<User> findAllEmployees() {
        return userRepository.findByUserType(UserType.EMPLOYEE);
    }

    // Get all administrators
    public List<User> findAllAdministrators() {
        return userRepository.findByUserType(UserType.ADMINISTRATOR);
    }

    // Get administrators without a company (available for assignment)
    public List<User> findAvailableAdministrators() {
        return userRepository.findByUserType(UserType.ADMINISTRATOR).stream()
                .filter(user -> user.getCompany() == null)
                .collect(java.util.stream.Collectors.toList());
    }

    // Get all superadmins
    public List<User> findAllSuperAdmins() {
        return userRepository.findByUserType(UserType.SUPERADMIN);
    }

    // Get users by company
    public List<User> findByCompanyId(int companyId) {
        return userRepository.findByCompanyId(companyId);
    }

    // Get employees by administrator
    public List<User> findEmployeesByAdministratorId(int administratorId) {
        return userRepository.findByAdministratorId(administratorId);
    }

    // Update employee vacation days
    public User updateEmployeeVacationDays(int employeeId, int total, int remaining) {
        User employee = findById(employeeId);
        if (employee != null && employee.getUserType() == UserType.EMPLOYEE) {
            employee.setVacationDaysTotal(total);
            employee.setVacationDaysRemaining(remaining);
            return userRepository.save(employee);
        }
        return null;
    }

    // Update employee workload
    public User updateEmployeeWorkload(int employeeId, float workload) {
        User employee = findById(employeeId);
        if (employee != null && employee.getUserType() == UserType.EMPLOYEE) {
            employee.setExpectedWorkload(workload);
            return userRepository.save(employee);
        }
        return null;
    }

    // Assign administrator to employee
    public User assignAdministratorToEmployee(int employeeId, int administratorId) {
        User employee = findById(employeeId);
        User admin = findById(administratorId);
        if (employee != null && admin != null 
            && employee.getUserType() == UserType.EMPLOYEE 
            && admin.getUserType() == UserType.ADMINISTRATOR) {
            employee.setAdministratorId(administratorId);
            return userRepository.save(employee);
        }
        return null;
    }

    public User createAdmin(CreateAdminRequest req) {
        User admin = new User();
        admin.setName(req.getName());
        admin.setEmail(req.getEmail());
        admin.setPassword(passwordEncoder.encode(req.getPassword()));
        admin.setAdministratorId(null);
        admin.setExpectedWorkload(0.0f);
        admin.setVacationDaysTotal(0);
        admin.setVacationDaysRemaining(0);
        admin.setCompany(null);  // Company will be set when the company is created
        admin.setUserType(UserType.ADMINISTRATOR);
        return userRepository.save(admin);
    }

    public Integer getAdminId(Integer employeeId) {
        Optional<User> user = userRepository.findById(employeeId);
        return user.get().getAdministratorId();
    }
}