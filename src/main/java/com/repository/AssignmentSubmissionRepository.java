package com.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.Assignment;
import com.entity.academic.AssignmentSubmission;
import com.entity.users.Student;

public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission, Long> {
    List<AssignmentSubmission> findByStudent(Student student);
    List<AssignmentSubmission> findByAssignment(Assignment assignment);
}
