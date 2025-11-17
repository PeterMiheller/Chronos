package com.example.chronos.DTO;

import java.time.LocalDate;

public class VacationRequest {

    private Integer employeeId;

    private Integer administratorId;

    private LocalDate startDate;

    private LocalDate endDate;

    private String reason;

    public VacationRequest() {}

    public VacationRequest(Integer employeeId, Integer administratorId, LocalDate startDate, LocalDate endDate, String reason) {
        this.employeeId = employeeId;
        this.administratorId = administratorId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }


    public Integer getEmployeeId() { return employeeId; }
    public void setEmployeeId(Integer employeeId) { this.employeeId = employeeId; }

    public Integer getAdministratorId() { return administratorId; }
    public void setAdministratorId(Integer administratorId) { this.administratorId = administratorId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
