package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.Exam;
import com.entity.academic.Result;
import com.entity.users.Student;

public interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudent(Student student);
    List<Result> findByStudentAndExam_Term(Student student, String term);
    List<Result> findByExam(Exam exam);
}
