package com.example.chronos.service;

import com.example.chronos.model.Company;
import com.example.chronos.model.TimesheetEntry;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.TimesheetEntryRepository;
import com.example.chronos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TimesheetServiceTest {

    @Mock
    private TimesheetEntryRepository timesheetEntryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TimesheetService timesheetService;

    private User testUser;
    private TimesheetEntry testEntry;
    private LocalDate testDate;

    @BeforeEach
    void setUp() {
        Company testCompany = new Company();
        testCompany.setId(1);
        testCompany.setName("Test Company");

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setUserType(UserType.EMPLOYEE);
        testUser.setCompany(testCompany);

        testDate = LocalDate.of(2024, 1, 15);

        testEntry = new TimesheetEntry(testUser, testDate, 8.0f);
        testEntry.setId(1);
    }

    @Test
    void getMonth_WithValidUser_ShouldReturnEntries() {
        // Arrange
        List<TimesheetEntry> entries = Arrays.asList(testEntry);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUserIdAndDateBetween(
                eq(1), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(entries);

        // Act
        List<TimesheetEntry> result = timesheetService.getMonth("test@example.com", 2024, 1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(8.0f, result.get(0).getHours());

        verify(userRepository).findByEmail("test@example.com");
        verify(timesheetEntryRepository).findByUserIdAndDateBetween(
                eq(1), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void getMonth_WithUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> timesheetService.getMonth("test@example.com", 2024, 1));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail("test@example.com");
        verify(timesheetEntryRepository, never()).findByUserIdAndDateBetween(
                anyInt(), any(LocalDate.class), any(LocalDate.class));
    }

    @Test
    void setHours_WithNewEntry_ShouldCreateEntry() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUserIdAndDate(1, testDate))
                .thenReturn(Optional.empty());
        when(timesheetEntryRepository.save(any(TimesheetEntry.class)))
                .thenReturn(testEntry);

        // Act
        TimesheetEntry result = timesheetService.setHours("test@example.com", testDate, 8.0f);

        // Assert
        assertNotNull(result);
        assertEquals(8.0f, result.getHours());

        verify(userRepository).findByEmail("test@example.com");
        verify(timesheetEntryRepository).findByUserIdAndDate(1, testDate);
        verify(timesheetEntryRepository).save(any(TimesheetEntry.class));
    }

    @Test
    void setHours_WithExistingEntry_ShouldUpdateEntry() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUserIdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));
        when(timesheetEntryRepository.save(any(TimesheetEntry.class)))
                .thenReturn(testEntry);

        // Act
        TimesheetEntry result = timesheetService.setHours("test@example.com", testDate, 10.0f);

        // Assert
        assertNotNull(result);
        verify(timesheetEntryRepository).save(testEntry);
    }

    @Test
    void setHours_WithNegativeHours_ShouldThrowException() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> timesheetService.setHours("test@example.com", testDate, -5.0f));
        assertEquals("Hours must be >= 0", exception.getMessage());

        verify(timesheetEntryRepository, never()).save(any(TimesheetEntry.class));
    }

    @Test
    void setHours_WithUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> timesheetService.setHours("test@example.com", testDate, 8.0f));
        assertEquals("User not found", exception.getMessage());

        verify(timesheetEntryRepository, never()).save(any(TimesheetEntry.class));
    }

    @Test
    void deleteEntry_WithExistingEntry_ShouldDeleteEntry() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUserIdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));

        // Act
        timesheetService.deleteEntry("test@example.com", testDate);

        // Assert
        verify(userRepository).findByEmail("test@example.com");
        verify(timesheetEntryRepository).findByUserIdAndDate(1, testDate);
        verify(timesheetEntryRepository).delete(testEntry);
    }

    @Test
    void deleteEntry_WithNoEntry_ShouldNotThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUserIdAndDate(1, testDate))
                .thenReturn(Optional.empty());

        // Act
        timesheetService.deleteEntry("test@example.com", testDate);

        // Assert
        verify(timesheetEntryRepository, never()).delete(any(TimesheetEntry.class));
    }

    @Test
    void deleteEntry_WithUserNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> timesheetService.deleteEntry("test@example.com", testDate));
        assertEquals("User not found", exception.getMessage());

        verify(timesheetEntryRepository, never()).delete(any(TimesheetEntry.class));
    }

    @Test
    void clockIn_WithNewEntry_ShouldCreateEntry() {
        // Arrange
        LocalTime clockInTime = LocalTime.of(9, 0);
        TimesheetEntry newEntry = new TimesheetEntry(testUser, testDate, 0.0f);
        newEntry.setClockInTime(clockInTime);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUser_IdAndDate(1, testDate))
                .thenReturn(Optional.empty());
        when(timesheetEntryRepository.save(any(TimesheetEntry.class)))
                .thenReturn(newEntry);

        // Act
        TimesheetEntry result = timesheetService.clockIn("test@example.com", testDate, clockInTime);

        // Assert
        assertNotNull(result);
        assertEquals(clockInTime, result.getClockInTime());

        verify(userRepository).findByEmail("test@example.com");
        verify(timesheetEntryRepository).save(any(TimesheetEntry.class));
    }

    @Test
    void clockIn_WhileAlreadyClockedIn_ShouldThrowException() {
        // Arrange
        LocalTime clockInTime = LocalTime.of(9, 0);
        testEntry.setClockInTime(clockInTime);
        testEntry.setClockOutTime(null); // Currently clocked in

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUser_IdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> timesheetService.clockIn("test@example.com", testDate, clockInTime));
        assertEquals("Already clocked in today", exception.getMessage());

        verify(timesheetEntryRepository, never()).save(any(TimesheetEntry.class));
    }

    @Test
    void clockOut_WithValidClockIn_ShouldUpdateEntry() {
        // Arrange
        LocalTime clockInTime = LocalTime.of(9, 0);
        LocalTime clockOutTime = LocalTime.of(17, 0);
        testEntry.setClockInTime(clockInTime);
        testEntry.setClockOutTime(null);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUser_IdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));
        when(timesheetEntryRepository.save(any(TimesheetEntry.class)))
                .thenReturn(testEntry);

        // Act
        TimesheetEntry result = timesheetService.clockOut("test@example.com", testDate, clockOutTime);

        // Assert
        assertNotNull(result);
        verify(timesheetEntryRepository).save(testEntry);
    }

    @Test
    void clockOut_WithoutClockIn_ShouldThrowException() {
        // Arrange
        LocalTime clockOutTime = LocalTime.of(17, 0);
        testEntry.setClockInTime(null);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUser_IdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> timesheetService.clockOut("test@example.com", testDate, clockOutTime));
        assertEquals("Must clock in first", exception.getMessage());
    }

    @Test
    void clockOut_WhenAlreadyClockedOut_ShouldThrowException() {
        // Arrange
        LocalTime clockInTime = LocalTime.of(9, 0);
        LocalTime clockOutTime = LocalTime.of(17, 0);
        testEntry.setClockInTime(clockInTime);
        testEntry.setClockOutTime(LocalTime.of(16, 0)); // Already clocked out

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(timesheetEntryRepository.findByUser_IdAndDate(1, testDate))
                .thenReturn(Optional.of(testEntry));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> timesheetService.clockOut("test@example.com", testDate, clockOutTime));
        assertEquals("Already clocked out", exception.getMessage());
    }
}
