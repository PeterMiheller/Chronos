package com.example.chronos.DTO;

public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;
    private Integer companyId;

    public AuthResponse() {
    }

    public AuthResponse(String token, String email, String name, String role) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public AuthResponse(String token, String email, String name, String role, Integer companyId) {
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
        this.companyId = companyId;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
