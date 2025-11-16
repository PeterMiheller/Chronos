package com.example.chronos.controller;

import com.example.chronos.DTO.CreateEventRequest;
import com.example.chronos.DTO.EventResponse;
import com.example.chronos.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(
            @RequestBody CreateEventRequest request,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            EventResponse response = eventService.createEvent(request, userEmail);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<EventResponse> events = eventService.getEventsForUserCompany(userEmail);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<EventResponse>> getEventsForMonth(
            @PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<EventResponse> events = eventService.getEventsForMonth(userEmail, year, month);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @PathVariable int eventId,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            eventService.deleteEvent(eventId, userEmail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
