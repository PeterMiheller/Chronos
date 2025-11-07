package com.example.chronos.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
public class SuperAdmin {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @OneToMany
    private List<Company> companies;

    private String password;
    private String email;

    public SuperAdmin() {}

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}
