package com.example.chronos.controller;

import com.example.chronos.DTO.TimesheetEntryResponse;
import com.example.chronos.DTO.TimesheetRequest;
import com.example.chronos.model.TimesheetEntry;
import com.example.chronos.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {
    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @GetMapping("/month/{year}/{month}")
    public ResponseEntity<List<TimesheetEntryResponse>> getMonth(
            @PathVariable int year,
            @PathVariable int month,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            List<TimesheetEntry> entries = timesheetService.getMonth(email, year, month);
            List<TimesheetEntryResponse> resp = entries.stream()
                    .map(e -> new TimesheetEntryResponse(e.getDate().toString(), e.getHours()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<TimesheetEntryResponse> setHours(
            @RequestBody TimesheetRequest request,
            Authentication authentication) {
        try {
            String email = authentication.getName();
            LocalDate date = LocalDate.parse(request.getDate());
            TimesheetEntry saved = timesheetService.setHours(email, date, request.getHours());
            return ResponseEntity.ok(new TimesheetEntryResponse(saved.getDate().toString(), saved.getHours()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{date}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String date, Authentication authentication) {
        try {
            String email = authentication.getName();
            LocalDate d = LocalDate.parse(date);
            timesheetService.deleteEntry(email, d);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
