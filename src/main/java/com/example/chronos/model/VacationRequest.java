package com.example.chronos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "vacation_requests")
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "employee_id", nullable = false)
    private int employeeId;

    @Column(name = "administrator_id", nullable = false)
    private int administratorId;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacationStatus status;

    @Column
    private String pdfPath;

    public VacationRequest() {}

    public VacationRequest(int employeeId, int administratorId, LocalDate startDate, LocalDate endDate) {
        this.employeeId = employeeId;
        this.administratorId = administratorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = VacationStatus.CREATED;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public int getAdministratorId() { return administratorId; }
    public void setAdministratorId(int administratorId) { this.administratorId = administratorId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public VacationStatus getStatus() { return status; }
    public void setStatus(VacationStatus status) { this.status = status; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}