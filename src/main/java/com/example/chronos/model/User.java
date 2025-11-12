package com.example.chronos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @JsonIgnoreProperties("users")
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

    @Override
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

    public  UserType getRole()
    {
        return userType;
    }

    // UserDetails implementation
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + userType.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
