package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SuperAdmin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany
    private List<Company> companies;

    public SuperAdmin() {}

    public int getId() {
        return id;
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
