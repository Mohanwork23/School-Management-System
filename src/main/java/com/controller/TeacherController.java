package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.*;
import com.dto.exam.ResultEntryDTO;
import com.security.AccessControlService;
import com.service.TeacherService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Teacher", description = "Teacher portal - assignments, attendance, grades, timetable and dashboard")
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;
    private final AccessControlService accessControlService;

    @Operation(summary = "Post assignment", description = "Create and assign a new assignment to a class")
    @PostMapping("/{teacherId}/assignment")
    public ResponseEntity<ApiResponse> postAssignment(
            @PathVariable String teacherId,
            @Valid @RequestBody AssignmentRequestDTO dto) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.postAssignment(teacherId, dto));
    }

    @Operation(summary = "Mark attendance", description = "Mark attendance for all students in a class")
    @PostMapping("/{teacherId}/attendance")
    public ResponseEntity<ApiResponse> markAttendance(
            @PathVariable String teacherId,
            @RequestBody AttendanceMarkDTO dto) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.markAttendance(teacherId, dto));
    }

    @PostMapping("/{teacherId}/grades")
    public ResponseEntity<ApiResponse> enterGrades(
            @PathVariable String teacherId,
            @RequestBody GradeEntryDTO dto) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.enterGrades(teacherId, dto));
    }

    @GetMapping("/{teacherId}/timetable")
    public ResponseEntity<ApiResponse> getTimetable(@PathVariable String teacherId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getTimetable(teacherId));
    }

    @GetMapping("/{teacherId}/dashboard")
    public ResponseEntity<ApiResponse> getAssignedData(@PathVariable String teacherId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getAssignedClassesAndSubjects(teacherId));
    }

    @PostMapping("/{teacherId}/publish-result")
    public ResponseEntity<ApiResponse> publishResult(
            @PathVariable String teacherId,
            @Valid @RequestBody ResultEntryDTO dto) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.publishResult(dto));
    }

    @GetMapping("/{teacherId}/exams")
    public ResponseEntity<ApiResponse> getExamsByClass(
            @PathVariable String teacherId,
            @RequestParam Long classId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getExamsByClass(classId));
    }

    @Operation(summary = "Teacher dashboard", description = "Returns classes, subjects and assignment count for the teacher")
    @GetMapping("/{teacherId}/full-dashboard")
    public ResponseEntity<ApiResponse> getTeacherDashboard(@PathVariable String teacherId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getTeacherDashboard(teacherId));
    }

    @Operation(summary = "Assignment submission tracker", description = "Returns who submitted and who hasn't for a given assignment")
    @GetMapping("/{teacherId}/assignment-tracker/{assignmentId}")
    public ResponseEntity<ApiResponse> getSubmissionTracker(
            @PathVariable String teacherId,
            @PathVariable Long assignmentId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getAssignmentSubmissionTracker(teacherId, assignmentId));
    }

    @Operation(summary = "Teacher profile", description = "Returns full profile of a teacher")
    @GetMapping("/{teacherId}/profile")
    public ResponseEntity<ApiResponse> getTeacherProfile(@PathVariable String teacherId) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.getTeacherProfile(teacherId));
    }

    @Operation(summary = "Update assignment", description = "Update an assignment created by the teacher")
    @PutMapping("/{teacherId}/assignments/{assignmentId}")
    public ResponseEntity<ApiResponse> updateAssignment(
            @PathVariable String teacherId,
            @PathVariable Long assignmentId,
            @Valid @RequestBody UpdateAssignmentDTO dto) {
        accessControlService.requireCurrentUser(teacherId);
        return ResponseEntity.ok(teacherService.updateAssignment(teacherId, assignmentId, dto));
    }
}
