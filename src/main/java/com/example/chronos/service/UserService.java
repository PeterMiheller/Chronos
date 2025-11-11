package com.example.chronos.service;

import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
}