package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.ClassRoom;
import com.entity.academic.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

	List<Exam> findByClassRoom(ClassRoom classRoom);

}
