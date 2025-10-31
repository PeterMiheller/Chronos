package com.example.chronos.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Admin extends User {

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Admin() {}

}
