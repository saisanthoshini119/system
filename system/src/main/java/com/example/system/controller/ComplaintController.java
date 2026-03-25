package com.example.system.controller;

import com.example.system.model.Complaint;
import com.example.system.model.User;
import com.example.system.repository.UserRepository;
import com.example.system.service.ComplaintService;
import com.example.system.service.FileUploadService;
import com.example.system.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/complaints")
@CrossOrigin(origins = "*")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createComplaint(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("priority") Complaint.Priority priority,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Authentication authentication) {
        
        try {
            User student = userRepository.findByEmail(authentication.getName()).get();
            String attachmentPath = null;
            if (file != null && !file.isEmpty()) {
                attachmentPath = fileUploadService.saveFile(file);
            }

            Complaint complaint = new Complaint();
            complaint.setTitle(title);
            complaint.setCategory(category);
            complaint.setDescription(description);
            complaint.setPriority(priority);
            complaint.setStudent(student);
            complaint.setAttachmentPath(attachmentPath);
            complaint.setComplaintId("CMP-" + System.currentTimeMillis()); // Basic ID generation

            return ResponseEntity.ok(complaintService.createComplaint(complaint));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getComplaints(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName()).get();
        if (user.getRole() == User.Role.STUDENT) {
            return ResponseEntity.ok(complaintService.getStudentComplaints(user));
        } else if (user.getRole() == User.Role.STAFF) {
            return ResponseEntity.ok(complaintService.getStaffComplaints(user));
        } else {
            return ResponseEntity.ok(complaintService.getAllComplaints());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam("status") Complaint.Status status,
            @RequestParam(value = "remarks", required = false) String remarks,
            Authentication authentication) {
        
        User user = userRepository.findByEmail(authentication.getName()).get();
        Complaint updatedComplaint = complaintService.updateComplaintStatus(id, status, remarks, user);

        if (updatedComplaint != null) {
            return ResponseEntity.ok(updatedComplaint);
        } else {
            return ResponseEntity.notFound().build(); // Or handle error appropriately
        }
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignComplaint(
            @PathVariable Long id,
            @RequestParam("staffId") Long staffId,
            Authentication authentication) {
        
        try {
            User staff = userRepository.findById(staffId)
                    .orElseThrow(() -> new RuntimeException("Staff not found"));
            Complaint updatedComplaint = complaintService.assignComplaint(id, staff);
            return ResponseEntity.ok(updatedComplaint);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
