package com.example.chronos.controller;

import com.example.chronos.DTO.*;
import com.example.chronos.model.TimesheetEntry;
import com.example.chronos.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {
    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @PostMapping("/clock-in")
    public ResponseEntity<?> clockIn(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            LocalDate date = LocalDate.parse(request.get("date"));
            LocalTime time = LocalTime.parse(request.get("time"));

            TimesheetEntry entry = timesheetService.clockIn(userEmail, date, time);

            return ResponseEntity.ok(new ClockInResponse(
                    entry.getId(),
                    entry.getUser().getId(),
                    entry.getClockInTime().toString(),
                    entry.getClockOutTime() != null ? entry.getClockOutTime().toString() : null,
                    entry.getDate().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @PostMapping("/clock-out")
    public ResponseEntity<?> clockOut(
            @RequestBody Map<String, String> request,
            Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            LocalDate date = LocalDate.parse(request.get("date"));
            LocalTime time = LocalTime.parse(request.get("time"));

            TimesheetEntry entry = timesheetService.clockOut(userEmail, date, time);

            return ResponseEntity.ok(new ClockOutResponse(
                    entry.getId(),
                    entry.getUser().getId(),
                    entry.getClockInTime().toString(),
                    entry.getClockOutTime().toString(),
                    entry.getDate().toString(),
                    entry.getHours()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @GetMapping("/today")
    public ResponseEntity<?> getTodayTimesheet(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            TimesheetEntry entry = timesheetService.getTodayTimesheet(userEmail);

            if (entry == null) {
                return ResponseEntity.ok(new TodayTimesheetResponse(
                        null,
                        null,
                        null,
                        null,
                        LocalDate.now().toString(),
                        null,
                        false
                ));
            }

            return ResponseEntity.ok(new TodayTimesheetResponse(
                    entry.getId(),
                    entry.getUser().getId(),
                    entry.getClockInTime() != null ? entry.getClockInTime().toString() : null,
                    entry.getClockOutTime() != null ? entry.getClockOutTime().toString() : null,
                    entry.getDate().toString(),
                    entry.getHours(),
                    entry.isClockedIn()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @GetMapping("/current-week")
    public ResponseEntity<?> getCurrentWeekTimesheet(Authentication authentication) {
        try {
            String userEmail = authentication.getName();
            List<TimesheetEntry> entries = timesheetService.getCurrentWeekTimesheet(userEmail);

            List<WeeklyHoursResponse> response = entries.stream()
                    .map(entry -> new WeeklyHoursResponse(
                            entry.getDate().toString(),
                            entry.getHours() != null ? entry.getHours() : 0f,
                            entry.isClockedIn()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
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
