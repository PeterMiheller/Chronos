package com.example.chronos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Act
        company.setId(1);

        // Assert
        assertEquals(1, company.getId());
    }

    @Test
    void setAndGetName_ShouldWorkCorrectly() {
        // Act
        company.setName("Test Company");

        // Assert
        assertEquals("Test Company", company.getName());
    }

    @Test
    void setAndGetAddress_ShouldWorkCorrectly() {
        // Act
        company.setAddress("123 Test Street");

        // Assert
        assertEquals("123 Test Street", company.getAddress());
    }

    @Test
    void createCompleteCompany_WithAllFields_ShouldWorkCorrectly() {
        // Act
        company.setId(1);
        company.setName("Tech Solutions Inc.");
        company.setAddress("456 Innovation Drive");

        // Assert
        assertEquals(1, company.getId());
        assertEquals("Tech Solutions Inc.", company.getName());
        assertEquals("456 Innovation Drive", company.getAddress());
    }
}
