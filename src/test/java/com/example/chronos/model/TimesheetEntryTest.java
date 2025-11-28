package com.example.chronos.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TimesheetEntryTest {

    private User testUser;
    private Company testCompany;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setName("Test Company");

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setCompany(testCompany);

        testDate = LocalDate.of(2024, 1, 15);
    }

    @Test
    void constructor_WithValidParameters_ShouldCreateEntry() {
        // Act
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 8.0f);

        // Assert
        assertNotNull(entry);
        assertEquals(testUser, entry.getUser());
        assertEquals(testDate, entry.getDate());
        assertEquals(8.0f, entry.getHours());
    }

    @Test
    void setHours_WithValidHours_ShouldUpdateHours() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 8.0f);

        // Act
        entry.setHours(10.0f);

        // Assert
        assertEquals(10.0f, entry.getHours());
    }

    @Test
    void isClockedIn_WhenClockedIn_ShouldReturnTrue() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 0.0f);
        entry.setClockInTime(LocalTime.of(9, 0));
        entry.setClockOutTime(null);

        // Act & Assert
        assertTrue(entry.isClockedIn());
    }

    @Test
    void isClockedIn_WhenClockedOut_ShouldReturnFalse() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 8.0f);
        entry.setClockInTime(LocalTime.of(9, 0));
        entry.setClockOutTime(LocalTime.of(17, 0));

        // Act & Assert
        assertFalse(entry.isClockedIn());
    }

    @Test
    void isClockedIn_WithNoClockInTime_ShouldReturnFalse() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 0.0f);
        entry.setClockInTime(null);
        entry.setClockOutTime(null);

        // Act & Assert
        assertFalse(entry.isClockedIn());
    }

    @Test
    void setClockInTime_ShouldUpdateClockInTime() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 0.0f);
        LocalTime clockInTime = LocalTime.of(9, 0);

        // Act
        entry.setClockInTime(clockInTime);

        // Assert
        assertEquals(clockInTime, entry.getClockInTime());
    }

    @Test
    void setClockOutTime_ShouldUpdateClockOutTime() {
        // Arrange
        TimesheetEntry entry = new TimesheetEntry(testUser, testDate, 0.0f);
        LocalTime clockOutTime = LocalTime.of(17, 0);

        // Act
        entry.setClockOutTime(clockOutTime);

        // Assert
        assertEquals(clockOutTime, entry.getClockOutTime());
    }
}
