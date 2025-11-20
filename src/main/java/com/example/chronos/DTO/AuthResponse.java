package com.example.chronos.DTO;

public class AuthResponse {
    private Integer id;
    private String token;
    private String email;
    private String name;
    private String role;
    private Integer companyId;
    private Integer administratorId;

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

    public AuthResponse(Integer id, Integer administratorId, String token, String email, String name, String role, Integer companyId) {
        this.id = id;
        this.administratorId = administratorId;
        this.token = token;
        this.email = email;
        this.name = name;
        this.role = role;
        this.companyId = companyId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getAdministratorId() {
        return administratorId;
    }
    public void setAdministratorId(Integer administratorId) {
        this.administratorId = administratorId;
    }
}
