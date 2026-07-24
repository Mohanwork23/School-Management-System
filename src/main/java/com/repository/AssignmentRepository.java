package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.Assignment;
import com.entity.academic.ClassRoom;
import com.entity.users.Teacher;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByClassRoom(ClassRoom classRoom);
    List<Assignment> findByClassRoomAndDueDateAfterOrderByDueDateAsc(ClassRoom classRoom, java.time.LocalDateTime now);
    long countByClassRoom(ClassRoom classRoom);
    long countByTeacher(Teacher teacher);
}
