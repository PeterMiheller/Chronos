package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("EMPLOYEE")
public class Employee extends User {

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "vacation_days_total")
    private int vacationDaysTotal;

    @Column(name = "vacation_days_remaining")
    private int vacationDaysRemaining;

    @Column(name = "expected_workload")
    private float expectedWorkload;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> vacationRequests;

    public Employee() {}

    public Employee(String email, String password, String name, Admin admin, Company company, int vacationDaysTotal, int vacationDaysRemaining, float expectedWorkload) {
        super(email, password, name);
        this.admin = admin;
        this.company = company;
        this.vacationDaysTotal = vacationDaysTotal;
        this.vacationDaysRemaining = vacationDaysRemaining;
        this.expectedWorkload = expectedWorkload;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getVacationDaysTotal() {
        return vacationDaysTotal;
    }

    public void setVacationDaysTotal(int vacationDaysTotal) {
        this.vacationDaysTotal = vacationDaysTotal;
    }

    public int getVacationDaysRemaining() {
        return vacationDaysRemaining;
    }

    public void setVacationDaysRemaining(int vacationDaysRemaining) {
        this.vacationDaysRemaining = vacationDaysRemaining;
    }

    public float getExpectedWorkload() {
        return expectedWorkload;
    }

    public void setExpectedWorkload(float expectedWorkload) {
        this.expectedWorkload = expectedWorkload;
    }

    public List<VacationRequest> getVacationRequests() {
        return vacationRequests;
    }

    public void setVacationRequests(List<VacationRequest> vacationRequests) {
        this.vacationRequests = vacationRequests;
    }
}
