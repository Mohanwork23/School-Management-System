package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.users.Parent;
import com.entity.users.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    List<Student> findByParent(Parent parent);
    List<Student> findByClassRoomId(Long classRoomId);
    List<Student> findByIsActive(boolean active);
    List<Student> findByFullNameContainingIgnoreCase(String name);
    Page<Student> findByFullNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Student> findAll(Pageable pageable);
}
