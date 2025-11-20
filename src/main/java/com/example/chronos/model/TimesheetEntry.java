package com.example.chronos.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "timesheet_entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "user_id", "work_date" })
})
public class TimesheetEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "work_date", nullable = false)
    private LocalDate date;

    @Column(name = "clock_in_time")
    private LocalTime clockInTime;

    @Column(name = "clock_out_time")
    private LocalTime clockOutTime;

    @Column(name = "hours", nullable = false)
    private Float hours = 0f;

    public TimesheetEntry() {
    }

    public TimesheetEntry(User user, LocalDate date) {
        this.user = user;
        this.date = date;
        this.hours = 0f;
    }

    public TimesheetEntry(User user, LocalDate date, Float hours) {
        this.user = user;
        this.date = date;
        this.hours = hours != null ? hours : 0f;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(LocalTime clockInTime) {
        this.clockInTime = clockInTime;
    }

    public LocalTime getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(LocalTime clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours != null ? hours : 0f;
    }

    public boolean isClockedIn() {
        return clockInTime != null && clockOutTime == null;
    }

    public Float calculateHours() {
        if (clockInTime != null && clockOutTime != null) {
            long seconds = java.time.temporal.ChronoUnit.SECONDS.between(clockInTime, clockOutTime);
            return (float) seconds / 3600;
        }
        return 0f;
    }
}
