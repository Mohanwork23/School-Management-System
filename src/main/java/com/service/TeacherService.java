package com.service;

import com.dto.ApiResponse;
import com.dto.AssignmentRequestDTO;
import com.dto.AttendanceMarkDTO;
import com.dto.GradeEntryDTO;
import com.dto.exam.ResultEntryDTO;

public interface TeacherService {

    ApiResponse postAssignment(String teacherId, AssignmentRequestDTO dto);

    ApiResponse markAttendance(String teacherId, AttendanceMarkDTO dto);

    ApiResponse enterGrades(String teacherId, GradeEntryDTO dto);

    ApiResponse getTimetable(String teacherId);

    ApiResponse getAssignedClassesAndSubjects(String teacherId);

    ApiResponse publishResult(ResultEntryDTO dto);

    ApiResponse getExamsByClass(Long classId);

    ApiResponse getTeacherDashboard(String teacherId);

    ApiResponse getAssignmentSubmissionTracker(String teacherId, Long assignmentId);
    ApiResponse getTeacherProfile(String teacherId);
}
