package com.example.chronos.service;

import com.example.chronos.model.TimesheetEntry;
import com.example.chronos.model.User;
import com.example.chronos.repository.TimesheetEntryRepository;
import com.example.chronos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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
}
