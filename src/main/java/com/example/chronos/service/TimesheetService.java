package com.example.chronos.service;

import com.example.chronos.model.TimesheetEntry;
import com.example.chronos.model.User;
import com.example.chronos.repository.TimesheetEntryRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class TimesheetService {
    private final TimesheetEntryRepository timesheetEntryRepository;
    private final UserRepository userRepository;

    public TimesheetService(TimesheetEntryRepository timesheetEntryRepository, UserRepository userRepository) {
        this.timesheetEntryRepository = timesheetEntryRepository;
        this.userRepository = userRepository;
    }

    public List<TimesheetEntry> getMonth(String userEmail, int year, int month) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return timesheetEntryRepository.findByUserIdAndDateBetween(user.getId(), start, end);
    }

    public TimesheetEntry setHours(String userEmail, LocalDate date, float hours) {
        if (hours < 0f)
            throw new IllegalArgumentException("Hours must be >= 0");
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");

        TimesheetEntry entry = timesheetEntryRepository
                .findByUserIdAndDate(user.getId(), date)
                .orElse(new TimesheetEntry(user, date, hours));
        entry.setHours(hours);
        return timesheetEntryRepository.save(entry);
    }

    public void deleteEntry(String userEmail, LocalDate date) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");
        timesheetEntryRepository.findByUserIdAndDate(user.getId(), date)
                .ifPresent(timesheetEntryRepository::delete);
    }

    public TimesheetEntry clockIn(String userEmail, LocalDate date, LocalTime time) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");

        Optional<TimesheetEntry> existing = timesheetEntryRepository.findByUser_IdAndDate(user.getId(), date);
        
        TimesheetEntry entry;
        if (existing.isPresent()) {
            entry = existing.get();
            // Only block if currently clocked in (clockIn set, clockOut null)
            if (entry.isClockedIn()) {
                throw new IllegalStateException("Already clocked in today");
            }
            // Resuming work: clear clockOutTime so it looks like we are currently working
            // We keep the existing 'hours' (accumulated from previous sessions)
            entry.setClockOutTime(null);
        } else {
            entry = new TimesheetEntry(user, date, 0f); // Initialize with 0 hours
        }

        // Set the new start time for this session
        entry.setClockInTime(time);
        return timesheetEntryRepository.save(entry);
    }

    public TimesheetEntry clockOut(String userEmail, LocalDate date, LocalTime time) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");

        TimesheetEntry entry = timesheetEntryRepository.findByUser_IdAndDate(user.getId(), date)
                .orElseThrow(() -> new RuntimeException("No clock in entry found for today"));

        if (entry.getClockInTime() == null) {
            throw new IllegalStateException("Must clock in first");
        }

        if (entry.getClockOutTime() != null) {
            throw new IllegalStateException("Already clocked out");
        }

        entry.setClockOutTime(time);
        
        // Calculate hours for THIS session
        float sessionHours = entry.calculateHours();
        
        // Add to previously accumulated hours
        float currentTotal = entry.getHours() != null ? entry.getHours() : 0f;
        entry.setHours(currentTotal + sessionHours);
        
        return timesheetEntryRepository.save(entry);
    }

    public TimesheetEntry getTodayTimesheet(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");

        return timesheetEntryRepository.findByUser_IdAndDate(user.getId(), LocalDate.now())
                .orElse(null);
    }

    public List<TimesheetEntry> getCurrentWeekTimesheet(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        if (user == null)
            throw new RuntimeException("User not found");

        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.US);
        LocalDate weekStart = today.with(weekFields.dayOfWeek(), 1); // Monday
        LocalDate weekEnd = today.with(weekFields.dayOfWeek(), 7);   // Sunday

        return timesheetEntryRepository.findByUserIdAndDateBetween(user.getId(), weekStart, weekEnd);
    }

    public Float getWeeklyHours(String userEmail) {
        List<TimesheetEntry> weekEntries = getCurrentWeekTimesheet(userEmail);
        return weekEntries.stream()
                .filter(entry -> entry.getHours() != null)
                .map(TimesheetEntry::getHours)
                .reduce(0f, Float::sum);
    }
}
