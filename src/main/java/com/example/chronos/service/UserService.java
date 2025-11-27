package com.example.chronos.service;

import com.example.chronos.DTO.CreateAdminRequest;
import com.example.chronos.DTO.CreateEmployeeRequest;
import com.example.chronos.DTO.UpdateEmployeeRequest;
import com.example.chronos.model.Company;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.CompanyRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
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

    // Get employees by company ID
    public List<User> findEmployeesByCompanyId(int companyId) {
        return userRepository.findByCompanyIdAndUserType(companyId, UserType.EMPLOYEE);
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

    public User createEmployee(User user) {
        // Set default values for employee
        user.setUserType(UserType.EMPLOYEE);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default vacation days if not provided
        if (user.getVacationDaysTotal() == null) {
            user.setVacationDaysTotal(0);
        }
        if (user.getVacationDaysRemaining() == null) {
            user.setVacationDaysRemaining(user.getVacationDaysTotal());
        }

        // Set default workload if not provided
        if (user.getExpectedWorkload() == null) {
            user.setExpectedWorkload(40.0f);
        }

        return userRepository.save(user);
    }

    public User createEmployee(CreateEmployeeRequest request) {
        User employee = new User();
        employee.setName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPassword(passwordEncoder.encode(request.getPassword()));
        employee.setUserType(UserType.EMPLOYEE);

        // Set company if provided
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId()).orElse(null);
            employee.setCompany(company);
        }

        // Set administrator if provided
        employee.setAdministratorId(request.getAdministratorId());

        // Set vacation days
        employee.setVacationDaysTotal(request.getVacationDaysTotal() != null ? request.getVacationDaysTotal() : 0);
        employee.setVacationDaysRemaining(employee.getVacationDaysTotal());

        // Set workload
        employee.setExpectedWorkload(request.getExpectedWorkload() != null ? request.getExpectedWorkload() : 40.0f);

        return userRepository.save(employee);
    }

    public User updateEmployee(int id, User user) {
        User existingEmployee = findById(id);
        if (existingEmployee == null || existingEmployee.getUserType() != UserType.EMPLOYEE) {
            return null;
        }

        // Update fields
        if (user.getName() != null) {
            existingEmployee.setName(user.getName());
        }
        if (user.getEmail() != null) {
            existingEmployee.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        if (user.getVacationDaysTotal() != null) {
            existingEmployee.setVacationDaysTotal(user.getVacationDaysTotal());
        }
        if (user.getVacationDaysRemaining() != null) {
            existingEmployee.setVacationDaysRemaining(user.getVacationDaysRemaining());
        }
        if (user.getExpectedWorkload() != null) {
            existingEmployee.setExpectedWorkload(user.getExpectedWorkload());
        }
        if (user.getAdministratorId() != null) {
            existingEmployee.setAdministratorId(user.getAdministratorId());
        }
        if (user.getCompany() != null) {
            existingEmployee.setCompany(user.getCompany());
        }

        return userRepository.save(existingEmployee);
    }

    public User updateEmployee(int id, UpdateEmployeeRequest request) {
        User existingEmployee = findById(id);
        if (existingEmployee == null || existingEmployee.getUserType() != UserType.EMPLOYEE) {
            return null;
        }

        // Update fields only if provided
        if (request.getName() != null) {
            existingEmployee.setName(request.getName());
        }
        if (request.getEmail() != null) {
            existingEmployee.setEmail(request.getEmail());
        }
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existingEmployee.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId()).orElse(null);
            existingEmployee.setCompany(company);
        }
        if (request.getAdministratorId() != null) {
            existingEmployee.setAdministratorId(request.getAdministratorId());
        }
        if (request.getVacationDaysTotal() != null) {
            existingEmployee.setVacationDaysTotal(request.getVacationDaysTotal());
        }
        if (request.getVacationDaysRemaining() != null) {
            existingEmployee.setVacationDaysRemaining(request.getVacationDaysRemaining());
        }
        if (request.getExpectedWorkload() != null) {
            existingEmployee.setExpectedWorkload(request.getExpectedWorkload());
        }

        return userRepository.save(existingEmployee);
    }

    public void deleteEmployee(int id) {
        User employee = findById(id);
        if (employee != null && employee.getUserType() == UserType.EMPLOYEE) {
            userRepository.deleteById(id);
        }
    }
}