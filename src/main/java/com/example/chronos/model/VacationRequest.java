package com.example.chronos.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class VacationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    private VacationStatus status;

    private String pdfPath;

    public VacationRequest() {}

    public int getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Admin getAdmin() {
        return admin;
    }

    public VacationStatus getStatus() {
        return status;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setStatus(VacationStatus status) {
        this.status = status;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}
