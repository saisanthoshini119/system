package com.example.system.dto;

import com.example.system.model.User;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private User.Role role;
    private String department;
    private String branch;

    public RegisterRequest() {}

    public RegisterRequest(String name, String email, String password, User.Role role, String department, String branch) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.department = department;
        this.branch = branch;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}
