package com.example.system.dto;

public class ProfileUpdateRequest {
    private String name;
    private String department;
    private String branch;
    private String password;

    public ProfileUpdateRequest() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
