package com.example.system.dto;

import com.example.system.model.User;

public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private User.Role role;
    private String department;

    public AuthResponse() {}

    public AuthResponse(String token, String name, String email, User.Role role, String department) {
        this.token = token;
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
}
