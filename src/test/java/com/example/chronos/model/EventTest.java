package com.example.chronos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventTest {

    private Event event;
    private Company company;
    private User user;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1);
        company.setName("Test Company");

        user = new User();
        user.setId(1);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setCompany(company);

        event = new Event();
    }

    @Test
    void setAndGetId_ShouldWorkCorrectly() {
        // Act
        event.setId(1);

        // Assert
        assertEquals(1, event.getId());
    }

    @Test
    void setAndGetType_ShouldWorkCorrectly() {
        // Act
        event.setType("TEAM_MEETING");

        // Assert
        assertEquals("TEAM_MEETING", event.getType());
    }

    @Test
    void setAndGetEventDateTime_ShouldWorkCorrectly() {
        // Arrange
        LocalDateTime dateTime = LocalDateTime.of(2024, 1, 15, 10, 0);

        // Act
        event.setEventDateTime(dateTime);

        // Assert
        assertEquals(dateTime, event.getEventDateTime());
    }

    @Test
    void setAndGetProjectName_ShouldWorkCorrectly() {
        // Act
        event.setProjectName("Project X");

        // Assert
        assertEquals("Project X", event.getProjectName());
    }

    @Test
    void setAndGetCompany_ShouldWorkCorrectly() {
        // Act
        event.setCompany(company);

        // Assert
        assertEquals(company, event.getCompany());
        assertEquals(1, event.getCompany().getId());
    }

    @Test
    void setAndGetCreatedBy_ShouldWorkCorrectly() {
        // Act
        event.setCreatedBy(user);

        // Assert
        assertEquals(user, event.getCreatedBy());
        assertEquals("Test User", event.getCreatedBy().getName());
    }

    @Test
    void setAndGetCreatedAt_ShouldWorkCorrectly() {
        // Arrange
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        event.setCreatedAt(createdAt);

        // Assert
        assertEquals(createdAt, event.getCreatedAt());
    }

    @Test
    void createCompleteEvent_WithAllFields_ShouldWorkCorrectly() {
        // Arrange
        LocalDateTime eventDateTime = LocalDateTime.of(2024, 1, 15, 10, 0);
        LocalDateTime createdAt = LocalDateTime.now();

        // Act
        event.setId(1);
        event.setType("PROJECT_DEADLINE");
        event.setEventDateTime(eventDateTime);
        event.setProjectName("Important Project");
        event.setCompany(company);
        event.setCreatedBy(user);
        event.setCreatedAt(createdAt);

        // Assert
        assertEquals(1, event.getId());
        assertEquals("PROJECT_DEADLINE", event.getType());
        assertEquals(eventDateTime, event.getEventDateTime());
        assertEquals("Important Project", event.getProjectName());
        assertEquals(company, event.getCompany());
        assertEquals(user, event.getCreatedBy());
        assertEquals(createdAt, event.getCreatedAt());
    }
}
