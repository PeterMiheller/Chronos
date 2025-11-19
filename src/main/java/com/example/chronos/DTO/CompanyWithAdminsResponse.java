package com.example.chronos.DTO;

import com.example.chronos.model.Company;
import com.example.chronos.model.User;

import java.util.List;

public class CompanyWithAdminsResponse {
    private int id;
    private String name;
    private String address;
    private List<AdminResponse> admins;

    public CompanyWithAdminsResponse() {}

    public CompanyWithAdminsResponse(Company company, List<User> admins) {
        this.id = company.getId();
        this.name = company.getName();
        this.address = company.getAddress();
        this.admins = admins.stream()
                .map(admin -> new AdminResponse(admin.getId(), admin.getName(), admin.getEmail()))
                .toList();
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

    public List<AdminResponse> getAdmins() {
        return admins;
    }

    public void setAdmins(List<AdminResponse> admins) {
        this.admins = admins;
    }

    // Inner class for admin details
    public static class AdminResponse {
        private int id;
        private String name;
        private String email;

        public AdminResponse() {}

        public AdminResponse(int id, String name, String email) {
            this.id = id;
            this.name = name;
            this.email = email;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}

