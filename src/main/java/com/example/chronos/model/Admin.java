package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "vacation_days_total")
    private int vacationDaysTotal;

    @Column(name = "vacation_days_remaining")
    private int vacationDaysRemaining;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VacationRequest> vacationRequests;

    public Admin() {}

    public Admin(String email, String password, String name, Company company, int vacationDaysTotal, int vacationDaysRemaining) {
        super(email, password, name);
        this.company = company;
        this.vacationDaysTotal = vacationDaysTotal;
        this.vacationDaysRemaining = vacationDaysRemaining;
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

    public List<VacationRequest> getVacationRequests() {
        return vacationRequests;
    }

    public void setVacationRequests(List<VacationRequest> vacationRequests) {
        this.vacationRequests = vacationRequests;
    }
}
