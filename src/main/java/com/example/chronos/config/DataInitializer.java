package com.example.chronos.config;

import com.example.chronos.model.Company;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.CompanyRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
            CompanyRepository companyRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if we already have users to avoid duplicates
            if (userRepository.count() > 0) {
                System.out.println("Database already initialized. Skipping data initialization.");
                return;
            }

            // Create a test company
            Company testCompany = new Company("Test Company", "123 Test Street");
            testCompany = companyRepository.save(testCompany);
            System.out.println("Created test company: " + testCompany.getName());

            // Create a SUPERADMIN user with encoded password
            User superAdmin = new User();
            superAdmin.setEmail("admin@test.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123")); // Password will be encoded
            superAdmin.setName("Super Admin");
            superAdmin.setCompany(testCompany);
            superAdmin.setUserType(UserType.SUPERADMIN);
            userRepository.save(superAdmin);
            System.out.println("Created SUPERADMIN user: " + superAdmin.getEmail() + " with password: admin123");

            // Create an ADMINISTRATOR user
            User admin = new User();
            admin.setEmail("manager@test.com");
            admin.setPassword(passwordEncoder.encode("manager123"));
            admin.setName("Manager User");
            admin.setCompany(testCompany);
            admin.setUserType(UserType.ADMINISTRATOR);
            userRepository.save(admin);
            System.out.println("Created ADMINISTRATOR user: " + admin.getEmail() + " with password: manager123");

            // Create an EMPLOYEE user
            User employee = new User();
            employee.setEmail("employee@test.com");
            employee.setPassword(passwordEncoder.encode("employee123"));
            employee.setName("Employee User");
            employee.setCompany(testCompany);
            employee.setUserType(UserType.EMPLOYEE);
            employee.setVacationDaysTotal(21);
            employee.setVacationDaysRemaining(21);
            employee.setExpectedWorkload(8.0f);
            employee.setAdministratorId(admin.getId());
            userRepository.save(employee);
            System.out.println("Created EMPLOYEE user: " + employee.getEmail() + " with password: employee123");

            System.out.println("=== Database initialization complete! ===");
            System.out.println("You can now login with:");
            System.out.println("  - admin@test.com / admin123 (SUPERADMIN)");
            System.out.println("  - manager@test.com / manager123 (ADMINISTRATOR)");
            System.out.println("  - employee@test.com / employee123 (EMPLOYEE)");
        };
    }
}
