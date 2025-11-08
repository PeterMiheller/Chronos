package com.example.chronos.service;

import com.example.chronos.repository.EmployeeRepository;
import com.example.chronos.repository.AdminRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(EmployeeRepository employeeRepository, AdminRepository adminRepository) {
        this.employeeRepository = employeeRepository;
        this.adminRepository = adminRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Try to find employee first, then admin
        return employeeRepository.findByEmail(email)
                .map(employee -> (UserDetails) employee)
                .orElseGet(() -> adminRepository.findByEmail(email)
                        .map(admin -> (UserDetails) admin)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email)));
    }
}
