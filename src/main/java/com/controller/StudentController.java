package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ApiResponse;
import com.service.StudentService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Student", description = "Student portal - grades, attendance, assignments, results and dashboard")
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/{studentId}/timetable")
    public ResponseEntity<ApiResponse> getTimetable(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getTimeTableForStudent(studentId));
    }

    @GetMapping("/{studentId}/grades")
    public ResponseEntity<ApiResponse> getGrades(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getGradesForStudent(studentId));
    }

    @GetMapping("/{studentId}/assignments")
    public ResponseEntity<ApiResponse> getAssignments(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getAssignmentsForStudent(studentId));
    }

    @PostMapping("/{studentId}/submit-assignment")
    public ResponseEntity<ApiResponse> submitAssignment(
            @PathVariable String studentId,
            @RequestParam Long assignmentId,
            @RequestParam String fileUrl) {
        return ResponseEntity.ok(studentService.submitAssignment(studentId, assignmentId, fileUrl));
    }

    @Operation(summary = "Get attendance", description = "Returns full attendance history for the student")
    @GetMapping("/{studentId}/attendance")
    public ResponseEntity<ApiResponse> getAttendance(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getAttendanceForStudent(studentId));
    }

    @Operation(summary = "Get fee status", description = "Returns all fee payments made by the student")
    @GetMapping("/{studentId}/fees")
    public ResponseEntity<ApiResponse> getFeeStatus(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getFeeStatus(studentId));
    }

    @GetMapping("/{studentId}/results")
    public ResponseEntity<ApiResponse> getStudentResults(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getResults(studentId));
    }

    @Operation(summary = "Get report card", description = "Returns full report card with grades and attendance summary")
    @GetMapping("/{studentId}/report-card")
    public ResponseEntity<ApiResponse> getReportCard(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getReportCard(studentId));
    }

    @Operation(summary = "Get student dashboard", description = "Returns assignment progress, attendance % and fee summary")
    @GetMapping("/{studentId}/dashboard")
    public ResponseEntity<ApiResponse> getStudentDashboard(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getStudentDashboard(studentId));
    }

    @GetMapping("/{studentId}/assignment-progress")
    public ResponseEntity<ApiResponse> getAssignmentSubmissionProgress(@PathVariable String studentId) {
        return ResponseEntity.ok(studentService.getAssignmentSubmissionProgress(studentId));
    }
}
