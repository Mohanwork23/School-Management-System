package com.service;

import com.dto.ApiResponse;

public interface StudentService {
    ApiResponse getTimeTableForStudent(String studentId);
    ApiResponse getGradesForStudent(String studentId);
    ApiResponse getAssignmentsForStudent(String studentId);
    ApiResponse submitAssignment(String studentId, Long assignmentId, String fileUrl);
    ApiResponse getAttendanceForStudent(String studentId);
    ApiResponse getFeeStatus(String studentId);
    ApiResponse getResults(String studentId);
    ApiResponse getReportCard(String studentId);
    ApiResponse getStudentDashboard(String studentId);
    ApiResponse getAssignmentSubmissionProgress(String studentId);
    ApiResponse getUpcomingExams(String studentId);
    ApiResponse getStudentProfile(String studentId);
    ApiResponse changePassword(String studentId, String oldPassword, String newPassword);
}
