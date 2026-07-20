package com.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.attendance.Attendance;
import com.entity.users.Student;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	List<Attendance> findByStudent(Student student);
	List<Attendance> findByStudentAndDateBetween(Student student, LocalDate startDate, LocalDate endDate);

}
