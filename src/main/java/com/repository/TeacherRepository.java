package com.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.users.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTeacherId(String teacherId);
    long count();
    Page<Teacher> findByFullNameContainingIgnoreCase(String name, Pageable pageable);
    List<Teacher> findByFullNameContainingIgnoreCase(String name);
    List<Teacher> findByDepartmentIgnoreCase(String department);
}
