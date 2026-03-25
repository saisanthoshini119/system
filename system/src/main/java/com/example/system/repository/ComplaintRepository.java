package com.example.system.repository;

import com.example.system.model.Complaint;
import com.example.system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    Optional<Complaint> findByComplaintId(String complaintId);
    List<Complaint> findByStudent(User student);
    List<Complaint> findByAssignedStaff(User staff);
    List<Complaint> findByStatus(Complaint.Status status);
    List<Complaint> findAllByOrderByCreatedAtDesc();
    List<Complaint> findByCategoryIgnoreCase(String category);
    List<Complaint> findByStudent_BranchIgnoreCase(String branch);
}
