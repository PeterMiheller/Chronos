package com.example.chronos.service;

import com.example.chronos.DTO.CreateEventRequest;
import com.example.chronos.DTO.EventResponse;
import com.example.chronos.model.Company;
import com.example.chronos.model.Event;
import com.example.chronos.model.User;
import com.example.chronos.model.UserType;
import com.example.chronos.repository.EventRepository;
import com.example.chronos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventService eventService;

    private User testUser;
    private Company testCompany;
    private Event testEvent;

    @BeforeEach
    void setUp() {
        testCompany = new Company();
        testCompany.setId(1);
        testCompany.setName("Test Company");

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");
        testUser.setUserType(UserType.ADMINISTRATOR);
        testUser.setCompany(testCompany);

        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setType("TEAM_MEETING");
        testEvent.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        testEvent.setProjectName(null);
        testEvent.setCompany(testCompany);
        testEvent.setCreatedBy(testUser);
    }

    @Test
    void createEvent_WithValidRequest_ShouldCreateEvent() {
        // Arrange
        CreateEventRequest request = new CreateEventRequest();
        request.setType("TEAM_MEETING");
        request.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        EventResponse response = eventService.createEvent(request, "test@example.com");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("TEAM_MEETING", response.getType());
        assertEquals(LocalDateTime.of(2024, 1, 15, 10, 0), response.getEventDateTime());
        assertEquals(1, response.getCompanyId());
        assertEquals("Test User", response.getCreatedByName());

        verify(userRepository).findByEmail("test@example.com");
        verify(eventRepository).save(any(Event.class));
    }

    @Test
    void createEvent_WithUserNotFound_ShouldThrowException() {
        // Arrange
        CreateEventRequest request = new CreateEventRequest();
        request.setType("TEAM_MEETING");
        request.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.createEvent(request, "test@example.com"));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail("test@example.com");
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void createEvent_WithUserWithoutCompany_ShouldThrowException() {
        // Arrange
        testUser.setCompany(null);
        CreateEventRequest request = new CreateEventRequest();
        request.setType("TEAM_MEETING");
        request.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.createEvent(request, "test@example.com"));
        assertEquals("User is not associated with a company", exception.getMessage());

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void createEvent_WithProjectDeadline_ShouldIncludeProjectName() {
        // Arrange
        CreateEventRequest request = new CreateEventRequest();
        request.setType("PROJECT_DEADLINE");
        request.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        request.setProjectName("Project X");

        Event deadlineEvent = new Event();
        deadlineEvent.setId(2);
        deadlineEvent.setType("PROJECT_DEADLINE");
        deadlineEvent.setEventDateTime(LocalDateTime.of(2024, 1, 15, 10, 0));
        deadlineEvent.setProjectName("Project X");
        deadlineEvent.setCompany(testCompany);
        deadlineEvent.setCreatedBy(testUser);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.save(any(Event.class))).thenReturn(deadlineEvent);

        // Act
        EventResponse response = eventService.createEvent(request, "test@example.com");

        // Assert
        assertNotNull(response);
        assertEquals("PROJECT_DEADLINE", response.getType());
        assertEquals("Project X", response.getProjectName());
    }

    @Test
    void getEventsForUserCompany_WithValidUser_ShouldReturnEvents() {
        // Arrange
        List<Event> events = Arrays.asList(testEvent);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findByCompanyId(1)).thenReturn(events);

        // Act
        List<EventResponse> responses = eventService.getEventsForUserCompany("test@example.com");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("TEAM_MEETING", responses.get(0).getType());

        verify(userRepository).findByEmail("test@example.com");
        verify(eventRepository).findByCompanyId(1);
    }

    @Test
    void getEventsForMonth_WithValidMonthAndYear_ShouldReturnEvents() {
        // Arrange
        List<Event> events = Arrays.asList(testEvent);
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findByCompanyIdAndEventDateTimeBetween(
                eq(1), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(events);

        // Act
        List<EventResponse> responses = eventService.getEventsForMonth("test@example.com", 2024, 1);

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());

        verify(userRepository).findByEmail("test@example.com");
        verify(eventRepository).findByCompanyIdAndEventDateTimeBetween(
                eq(1), any(LocalDateTime.class), any(LocalDateTime.class));
    }

    @Test
    void deleteEvent_WithValidEventAndUser_ShouldDeleteEvent() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findById(1)).thenReturn(Optional.of(testEvent));

        // Act
        eventService.deleteEvent(1, "test@example.com");

        // Assert
        verify(userRepository).findByEmail("test@example.com");
        verify(eventRepository).findById(1);
        verify(eventRepository).delete(testEvent);
    }

    @Test
    void deleteEvent_WithEventNotFound_ShouldThrowException() {
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findById(1)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.deleteEvent(1, "test@example.com"));
        assertEquals("Event not found", exception.getMessage());

        verify(eventRepository, never()).delete(any(Event.class));
    }

    @Test
    void deleteEvent_WithUserFromDifferentCompany_ShouldThrowException() {
        // Arrange
        Company otherCompany = new Company();
        otherCompany.setId(2);
        otherCompany.setName("Other Company");
        testUser.setCompany(otherCompany);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findById(1)).thenReturn(Optional.of(testEvent));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.deleteEvent(1, "test@example.com"));
        assertEquals("User is not authorized to delete this event", exception.getMessage());

        verify(eventRepository, never()).delete(any(Event.class));
    }

    @Test
    void deleteEvent_WithUserWithoutCompany_ShouldThrowException() {
        // Arrange
        testUser.setCompany(null);

        when(userRepository.findByEmail("test@example.com")).thenReturn(testUser);
        when(eventRepository.findById(1)).thenReturn(Optional.of(testEvent));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> eventService.deleteEvent(1, "test@example.com"));
        assertEquals("User is not authorized to delete this event", exception.getMessage());

        verify(eventRepository, never()).delete(any(Event.class));
    }
}
