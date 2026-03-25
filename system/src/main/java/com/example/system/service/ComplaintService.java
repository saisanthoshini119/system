package com.example.system.service;

import com.example.system.model.Complaint;
import com.example.system.model.ComplaintUpdate;
import com.example.system.model.Notification;
import com.example.system.model.User;
import com.example.system.repository.ComplaintRepository;
import com.example.system.repository.ComplaintUpdateRepository;
import com.example.system.repository.NotificationRepository;
import com.example.system.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintUpdateRepository updateRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    public Complaint createComplaint(Complaint complaint) {
        complaint.setStatus(Complaint.Status.PENDING);
        complaint.setComplaintId("CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        Complaint saved = complaintRepository.save(complaint);
        notificationService.createNotification(complaint.getStudent(), "Your complaint " + saved.getComplaintId() + " has been submitted.");
        return saved;
    }

    public List<Complaint> getStudentComplaints(User student) {
        return complaintRepository.findByStudent(student);
    }

    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Complaint> getStaffComplaints(User staff) {
        String branch = staff.getBranch();
        if (branch == null || branch.isBlank()) {
            // If branch is not set, restrict visibility to complaints explicitly assigned
            // to this staff member instead of exposing all branches.
            return complaintRepository.findByAssignedStaff(staff);
        }
        return complaintRepository.findByStudent_BranchIgnoreCase(branch);
    }

    @Transactional
    public Complaint updateComplaintStatus(Long id, Complaint.Status status, String remarks, User updatedBy) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        complaint.setStatus(status);
        if (status == Complaint.Status.ASSIGNED && complaint.getAssignedStaff() != null) {
            // Notification logic can be added here
        }
        
        complaintRepository.save(complaint);

        ComplaintUpdate update = new ComplaintUpdate(complaint, updatedBy, status, remarks);
        updateRepository.save(update);

        // Notify student
        String message = "Your complaint " + complaint.getComplaintId() + " status updated to " + status;
        if (status == Complaint.Status.VERIFIED) {
            message = "your problem has verified";
        }
        
        notificationService.createNotification(complaint.getStudent(), message);

        return complaint;
    }

    @Transactional
    public Complaint assignComplaint(Long id, User staff) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setAssignedStaff(staff);
        complaint.setStatus(Complaint.Status.ASSIGNED);
        
        notificationRepository.save(new Notification(staff, 
                "New complaint assigned to you: " + complaint.getComplaintId()));
                
        return complaintRepository.save(complaint);
    }
}
