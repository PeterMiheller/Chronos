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

    public Company createCompany(String name, String address) {
        Company company = new Company(name, address);
        return companyRepository.save(company);
    }

    public Company getDefaultCompany() {
        List<Company> companies = companyRepository.findAll();
        if (companies.isEmpty()) {
            // Create a default company if none exists
            return createCompany("Default Company", "Default Address");
        }
        return companies.get(0);
    }

    public List<CompanyWithAdminsResponse> getAllCompaniesWithAdmins() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream()
                .map(company -> {
                    List<User> admins = userRepository.findByCompanyId(company.getId()).stream()
                            .filter(user -> user.getUserType() == UserType.ADMINISTRATOR || user.getUserType() == UserType.SUPERADMIN)
                            .collect(Collectors.toList());
                    return new CompanyWithAdminsResponse(company, admins);
                })
                .collect(Collectors.toList());
    }
}