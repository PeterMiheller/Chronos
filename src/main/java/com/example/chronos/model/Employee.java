package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Employee extends User{

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    private int vacationDaysTotal;
    private int vacationDaysRemaining;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    private List<VacationRequest> vacationRequests;

    public Employee() {
        super();
    }

    public Admin getAdmin() {
        return admin;
    }

    public int getVacationDaysTotal() {
        return vacationDaysTotal;
    }

    public int getVacationDaysRemaining() {
        return vacationDaysRemaining;
    }

    public List<VacationRequest> getVacationRequests() {
        return vacationRequests;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setVacationDaysTotal(int vacationDaysTotal) {
        this.vacationDaysTotal = vacationDaysTotal;
    }

    public void setVacationDaysRemaining(int vacationDaysRemaining) {
        this.vacationDaysRemaining = vacationDaysRemaining;
    }

    public void setVacationRequests(List<VacationRequest> vacationRequests) {
        this.vacationRequests = vacationRequests;
    }
}
