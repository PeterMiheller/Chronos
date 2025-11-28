package com.example.chronos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1);
        company.setName("Test Company");

        user = new User();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Act
        user.setId(1);

        // Assert
        assertEquals(1, user.getId());
    }

    @Test
    void setAndGetEmail_ShouldWorkCorrectly() {
        // Act
        user.setEmail("test@example.com");

        // Assert
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Act
        user.setName("Test User");

        // Assert
        assertEquals("Test User", user.getName());
    }

    @Test
    void setAndGetPassword_ShouldWorkCorrectly() {
        // Act
        user.setPassword("hashedPassword");

        // Assert
        assertEquals("hashedPassword", user.getPassword());
    }

    @Test
    void setAndGetUserType_ShouldWorkCorrectly() {
        // Act
        user.setUserType(UserType.EMPLOYEE);

        // Assert
        assertEquals(UserType.EMPLOYEE, user.getUserType());
    }

    @Test
    void setAndGetCompany_ShouldWorkCorrectly() {
        // Act
        user.setCompany(company);

        // Assert
        assertEquals(company, user.getCompany());
        assertEquals(1, user.getCompany().getId());
    }

    @Test
    void setAndGetAdministratorId_ShouldWorkCorrectly() {
        // Act
        user.setAdministratorId(5);

        // Assert
        assertEquals(5, user.getAdministratorId());
    }

    @Test
    void setAndGetVacationDaysTotal_ShouldWorkCorrectly() {
        // Act
        user.setVacationDaysTotal(20);

        // Assert
        assertEquals(20, user.getVacationDaysTotal());
    }

    @Test
    void setAndGetVacationDaysRemaining_ShouldWorkCorrectly() {
        // Act
        user.setVacationDaysRemaining(15);

        // Assert
        assertEquals(15, user.getVacationDaysRemaining());
    }

    @Test
    void setAndGetExpectedWorkload_ShouldWorkCorrectly() {
        // Act
        user.setExpectedWorkload(40.0f);

        // Assert
        assertEquals(40.0f, user.getExpectedWorkload());
    }

    @Test
    void createCompleteEmployee_WithAllFields_ShouldWorkCorrectly() {
        // Act
        user.setId(1);
        user.setEmail("employee@example.com");
        user.setName("John Doe");
        user.setPassword("hashedPassword123");
        user.setUserType(UserType.EMPLOYEE);
        user.setCompany(company);
        user.setAdministratorId(10);
        user.setVacationDaysTotal(20);
        user.setVacationDaysRemaining(15);
        user.setExpectedWorkload(40.0f);

        // Assert
        assertEquals(1, user.getId());
        assertEquals("employee@example.com", user.getEmail());
        assertEquals("John Doe", user.getName());
        assertEquals("hashedPassword123", user.getPassword());
        assertEquals(UserType.EMPLOYEE, user.getUserType());
        assertEquals(company, user.getCompany());
        assertEquals(10, user.getAdministratorId());
        assertEquals(20, user.getVacationDaysTotal());
        assertEquals(15, user.getVacationDaysRemaining());
        assertEquals(40.0f, user.getExpectedWorkload());
    }

    @Test
    void createAdministrator_ShouldHaveCorrectRole() {
        // Act
        user.setUserType(UserType.ADMINISTRATOR);
        user.setName("Admin User");
        user.setEmail("admin@example.com");

        // Assert
        assertEquals(UserType.ADMINISTRATOR, user.getUserType());
        assertEquals("Admin User", user.getName());
    }

    @Test
    void createSuperAdmin_ShouldHaveCorrectRole() {
        // Act
        user.setUserType(UserType.SUPERADMIN);
        user.setName("Super Admin");
        user.setEmail("superadmin@example.com");

        // Assert
        assertEquals(UserType.SUPERADMIN, user.getUserType());
        assertEquals("Super Admin", user.getName());
    }

    @Test
    void getRole_ShouldReturnUserType() {
        // Act
        user.setUserType(UserType.EMPLOYEE);

        // Assert
        assertEquals(UserType.EMPLOYEE, user.getRole());
    }
}
