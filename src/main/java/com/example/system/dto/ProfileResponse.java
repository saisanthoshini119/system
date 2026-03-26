package com.example.system.dto;

import com.example.system.model.User;

public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private User.Role role;
    private String department;
    private String branch;

    public ProfileResponse() {}

    public ProfileResponse(Long id, String name, String email, User.Role role, String department, String branch) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.department = department;
        this.branch = branch;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
}
