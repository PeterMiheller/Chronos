package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("SUPERADMIN")
public class SuperAdmin extends User {

    @OneToMany(mappedBy = "superAdmin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Company> companies;

    public SuperAdmin() {}

    public SuperAdmin(String email, String password, String name) {
        super(email, password, name);
    }

    public List<Company> getCompanies() {
        return companies;
    }

    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }
}
