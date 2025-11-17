package com.example.chronos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

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

    @Column(name = "hours", nullable = false)
    private Float hours;

    public TimesheetEntry() {
    }

    public TimesheetEntry(User user, LocalDate date, Float hours) {
        this.user = user;
        this.date = date;
        this.hours = hours;
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

    public Float getHours() {
        return hours;
    }

    public void setHours(Float hours) {
        this.hours = hours;
    }
}
