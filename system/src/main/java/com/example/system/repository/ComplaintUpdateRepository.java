package com.example.system.repository;

import com.example.system.model.Complaint;
import com.example.system.model.ComplaintUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplaintUpdateRepository extends JpaRepository<ComplaintUpdate, Long> {
    List<ComplaintUpdate> findByComplaintOrderByUpdateTimeDesc(Complaint complaint);
}
