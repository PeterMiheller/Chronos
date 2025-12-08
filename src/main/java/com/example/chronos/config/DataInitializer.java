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
            if (userRepository.count() > 0) {
                System.out.println("Database already initialized. Skipping data initialization.");
                return;
            }

            // Create test company
            Company testCompany = new Company("Test Company", "123 Test Street");
            testCompany = companyRepository.save(testCompany);

            // SUPERADMIN
            User superAdmin = new User();
            superAdmin.setEmail("admin@test.com");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setName("Super Admin");
            superAdmin.setCompany(testCompany);
            superAdmin.setUserType(UserType.SUPERADMIN);
            superAdmin = userRepository.save(superAdmin); // MUST be saved
            System.out.println("Created SUPERADMIN user: admin@test.com / admin123");

            // ADMINISTRATOR
            User admin = new User();
            admin.setEmail("manager@test.com");
            admin.setPassword(passwordEncoder.encode("manager123"));
            admin.setName("Manager User");
            admin.setCompany(testCompany);
            admin.setUserType(UserType.ADMINISTRATOR);
            admin.setVacationDaysTotal(25);
            admin.setVacationDaysRemaining(25);
            admin.setExpectedWorkload(40.0f);
            admin = userRepository.save(admin); // MUST be saved
            System.out.println("Created ADMIN user: manager@test.com / manager123");

            // EMPLOYEE
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
            employee = userRepository.save(employee);

            System.out.println("Created EMPLOYEE user: employee@test.com / employee123");
            System.out.println("(Employee administratorId = " + admin.getId() + ")");

            System.out.println("=== Database initialization complete! ===");
        };
    }
}
