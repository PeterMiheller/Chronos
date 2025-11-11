package com.example.chronos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    // Employee-specific fields
    @Column(name = "vacation_days_total")
    private Integer vacationDaysTotal;

    @Column(name = "vacation_days_remaining")
    private Integer vacationDaysRemaining;

    @Column(name = "expected_workload")
    private Float expectedWorkload;

    @Column(name = "administrator_id")
    private Integer administratorId;

    public User() {}

    public User(String email, String password, String name, Company company, UserType userType) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.company = company;
        this.userType = userType;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public Integer getVacationDaysTotal() { return vacationDaysTotal; }
    public void setVacationDaysTotal(Integer vacationDaysTotal) { 
        this.vacationDaysTotal = vacationDaysTotal; 
    }

    public Integer getVacationDaysRemaining() { return vacationDaysRemaining; }
    public void setVacationDaysRemaining(Integer vacationDaysRemaining) { 
        this.vacationDaysRemaining = vacationDaysRemaining; 
    }

    public Float getExpectedWorkload() { return expectedWorkload; }
    public void setExpectedWorkload(Float expectedWorkload) { 
        this.expectedWorkload = expectedWorkload; 
    }

    public Integer getAdministratorId() { return administratorId; }
    public void setAdministratorId(Integer administratorId) { 
        this.administratorId = administratorId; 
    }
}