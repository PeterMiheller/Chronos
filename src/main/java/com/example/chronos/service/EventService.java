package com.example.chronos.service;

import com.example.chronos.DTO.CreateEventRequest;
import com.example.chronos.DTO.EventResponse;
import com.example.chronos.model.Event;
import com.example.chronos.model.User;
import com.example.chronos.repository.EventRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventResponse createEvent(CreateEventRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getCompany() == null) {
            throw new RuntimeException("User is not associated with a company");
        }

        Event event = new Event();
        event.setType(request.getType());
        event.setEventDateTime(request.getEventDateTime());
        event.setProjectName(request.getProjectName());
        event.setCompany(user.getCompany());
        event.setCreatedBy(user);

        Event savedEvent = eventRepository.save(event);
        return convertToResponse(savedEvent);
    }

    public List<EventResponse> getEventsForUserCompany(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getCompany() == null) {
            throw new RuntimeException("User is not associated with a company");
        }

        List<Event> events = eventRepository.findByCompanyId(user.getCompany().getId());
        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<EventResponse> getEventsForMonth(String userEmail, int year, int month) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.getCompany() == null) {
            throw new RuntimeException("User is not associated with a company");
        }

        LocalDateTime startOfMonth = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.plusMonths(1).minusSeconds(1);

        List<Event> events = eventRepository.findByCompanyIdAndEventDateTimeBetween(
                user.getCompany().getId(), startOfMonth, endOfMonth);

        return events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public void deleteEvent(int eventId, String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // Check if user is from the same company as the event
        if (user.getCompany() == null || user.getCompany().getId() != event.getCompany().getId()) {
            throw new RuntimeException("User is not authorized to delete this event");
        }

        eventRepository.delete(event);
    }

    private EventResponse convertToResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getType(),
                event.getEventDateTime(),
                event.getProjectName(),
                event.getCompany().getId(),
                event.getCreatedBy().getName());
    }
}
