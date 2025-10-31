package com.example.chronos.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String companyName;
    private String companyAddress;

    @ManyToOne
    @JoinColumn(name = "superadmin_id")
    private SuperAdmin superAdmin;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Admin> admins;



}
