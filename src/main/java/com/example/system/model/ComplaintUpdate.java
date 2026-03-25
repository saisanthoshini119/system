package com.example.system.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaint_updates")
public class ComplaintUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "complaint_id", nullable = false)
    private Complaint complaint;

    @ManyToOne
    @JoinColumn(name = "updated_by", nullable = false)
    private User updatedBy;

    @Enumerated(EnumType.STRING)
    private Complaint.Status status;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    public ComplaintUpdate() {}

    public ComplaintUpdate(Complaint complaint, User updatedBy, Complaint.Status status, String remarks) {
        this.complaint = complaint;
        this.updatedBy = updatedBy;
        this.status = status;
        this.remarks = remarks;
    }

    @PrePersist
    protected void onCreate() {
        updateTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Complaint getComplaint() { return complaint; }
    public void setComplaint(Complaint complaint) { this.complaint = complaint; }
    public User getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(User updatedBy) { this.updatedBy = updatedBy; }
    public Complaint.Status getStatus() { return status; }
    public void setStatus(Complaint.Status status) { this.status = status; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public LocalDateTime getUpdateTime() { return updateTime; }
}
