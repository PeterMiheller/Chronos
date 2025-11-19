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
        companyRepository.deleteById(id);
    }

    public Company createCompany(String name, String address, Integer adminId) {
        Company company = new Company(name, address);
        company = companyRepository.save(company);

        // If an admin ID is provided, link the admin to this company
        if (adminId != null) {
            User admin = userRepository.findById(adminId).orElse(null);
            if (admin != null && admin.getUserType() == UserType.ADMINISTRATOR) {
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