package com.example.chronos.DTO;

import com.example.chronos.model.Company;
import com.example.chronos.model.User;

public class CompanyWithAdminsResponse {
    private int id;
    private String name;
    private String address;
    private String adminContactName;
    private String adminContactEmail;

    public CompanyWithAdminsResponse() {}

    public CompanyWithAdminsResponse(Company company, User admin) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        if (admin != null) {
            this.adminContactName = admin.getName();
            this.adminContactEmail = admin.getEmail();
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdminContactName() {
        return adminContactName;
    }

    public void setAdminContactName(String adminContactName) {
        this.adminContactName = adminContactName;
    }

    public String getAdminContactEmail() {
        return adminContactEmail;
    }

    public void setAdminContactEmail(String adminContactEmail) {
        this.adminContactEmail = adminContactEmail;
    }
}

