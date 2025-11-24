package com.example.chronos.service;

import com.example.chronos.DTO.CompanyWithAdminsResponse;
import com.example.chronos.model.Company;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.CompanyRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(int id) {
        return companyRepository.findById(id).orElse(null);
    }

    public void deleteById(int id) {
        // First, unlink all users from this company
        List<User> users = userRepository.findByCompanyId(id);
        for (User user : users) {
            user.setCompany(null);
            userRepository.save(user);
        }

        // Now delete the company
        companyRepository.deleteById(id);
    }

    public Company createCompany(String name, String address, Integer adminId) {
        Company company = new Company(name, address);
        company = companyRepository.save(company);

        // If an admin ID is provided, link the admin to this company
        if (adminId != null) {
            User admin = userRepository.findById(adminId).orElse(null);
            if (admin != null && admin.getUserType() == UserType.ADMINISTRATOR) {
                // Check if admin is already assigned to another company
                if (admin.getCompany() != null) {
                    throw new IllegalStateException("Admin is already assigned to company: " + admin.getCompany().getName());
                }
                admin.setCompany(company);
                userRepository.save(admin);
            }
        }

        return company;
    }

    public Company getDefaultCompany() {
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            // Create a default company if none exists
            return createCompany("Default Company", "Default Address", null);
        }
        return companies.get(0);
    }

    public Company updateCompany(int id, String name, String address, Integer newAdminId) {
        Company company = findById(id);
        if (company == null) {
            return null;
        }

        // Update company details
        company.setName(name);
        company.setAddress(address);
        company = companyRepository.save(company);

        // Handle admin reassignment if a new admin ID is provided
        if (newAdminId != null) {
            // Find the current admin for this company
            User currentAdmin = userRepository.findByCompanyId(id).stream()
                    .filter(user -> user.getUserType() == UserType.ADMINISTRATOR)
                    .findFirst()
                    .orElse(null);

            // Find the new admin
            User newAdmin = userRepository.findById(newAdminId).orElse(null);

            // Only proceed if the new admin exists and is different from current admin
            if (newAdmin != null && newAdmin.getUserType() == UserType.ADMINISTRATOR) {
                // Check if new admin is already assigned to another company
                if (newAdmin.getCompany() != null && newAdmin.getCompany().getId() != id) {
                    throw new IllegalStateException("Admin is already assigned to company: " + newAdmin.getCompany().getName());
                }

                // Unlink current admin if exists and is different from new admin
                if (currentAdmin != null && currentAdmin.getId() != newAdminId) {
                    currentAdmin.setCompany(null);
                    userRepository.save(currentAdmin);
                }

                // Link new admin to this company
                if (newAdmin.getCompany() == null || newAdmin.getCompany().getId() != id) {
                    newAdmin.setCompany(company);
                    userRepository.save(newAdmin);
                }
            }
        }

        return company;
    }

    public List<CompanyWithAdminsResponse> getAllCompaniesWithAdmin() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> {
                    // Find the ADMINISTRATOR for this company (only ADMINISTRATOR, not SUPERADMIN)
                    User admin = userRepository.findByCompanyId(company.getId()).stream()
                            .filter(user -> user.getUserType() == UserType.ADMINISTRATOR)
                            .findFirst()
                            .orElse(null);

                    return new CompanyWithAdminsResponse(company, admin);
                })
                .collect(Collectors.toList());
    }
}