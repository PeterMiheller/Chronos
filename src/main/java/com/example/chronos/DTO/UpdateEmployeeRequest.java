package com.example.chronos.DTO;

public class UpdateEmployeeRequest {
    private String name;
    private String email;
    private String password;
    private Integer companyId;
    private Integer administratorId;
    private Integer vacationDaysTotal;
    private Integer vacationDaysRemaining;
    private Float expectedWorkload;

    public UpdateEmployeeRequest() {}

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getCompanyId() { return companyId; }
    public void setCompanyId(Integer companyId) { this.companyId = companyId; }

    public Integer getAdministratorId() { return administratorId; }
    public void setAdministratorId(Integer administratorId) { this.administratorId = administratorId; }

    public Integer getVacationDaysTotal() { return vacationDaysTotal; }
    public void setVacationDaysTotal(Integer vacationDaysTotal) { this.vacationDaysTotal = vacationDaysTotal; }

    public Integer getVacationDaysRemaining() { return vacationDaysRemaining; }
    public void setVacationDaysRemaining(Integer vacationDaysRemaining) { this.vacationDaysRemaining = vacationDaysRemaining; }

    public Float getExpectedWorkload() { return expectedWorkload; }
    public void setExpectedWorkload(Float expectedWorkload) { this.expectedWorkload = expectedWorkload; }
}

