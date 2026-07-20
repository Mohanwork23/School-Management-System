package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.*;
import com.dto.exam.ResultEntryDTO;
import com.service.TeacherService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@CrossOrigin("*")
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping("/{teacherId}/assignment")
    public ResponseEntity<ApiResponse> postAssignment(
            @PathVariable String teacherId,
            @RequestBody AssignmentRequestDTO dto) {
        return ResponseEntity.ok(teacherService.postAssignment(teacherId, dto));
    }

    @PostMapping("/{teacherId}/attendance")
    public ResponseEntity<ApiResponse> markAttendance(
            @PathVariable String teacherId,
            @RequestBody AttendanceMarkDTO dto) {
        return ResponseEntity.ok(teacherService.markAttendance(teacherId, dto));
    }

    @PostMapping("/{teacherId}/grades")
    public ResponseEntity<ApiResponse> enterGrades(
            @PathVariable String teacherId,
            @RequestBody GradeEntryDTO dto) {
        return ResponseEntity.ok(teacherService.enterGrades(teacherId, dto));
    }

    @GetMapping("/{teacherId}/timetable")
    public ResponseEntity<ApiResponse> getTimetable(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTimetable(teacherId));
    }

    @GetMapping("/{teacherId}/dashboard")
    public ResponseEntity<ApiResponse> getAssignedData(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getAssignedClassesAndSubjects(teacherId));
    }

    @PostMapping("/{teacherId}/publish-result")
    public ResponseEntity<ApiResponse> publishResult(
            @RequestBody ResultEntryDTO dto) {
        return ResponseEntity.ok(teacherService.publishResult(dto));
    }

    @GetMapping("/{teacherId}/exams")
    public ResponseEntity<ApiResponse> getExamsByClass(
            @PathVariable String teacherId,
            @RequestParam Long classId) {
        return ResponseEntity.ok(teacherService.getExamsByClass(classId));
    }

    @GetMapping("/{teacherId}/full-dashboard")
    public ResponseEntity<ApiResponse> getTeacherDashboard(@PathVariable String teacherId) {
        return ResponseEntity.ok(teacherService.getTeacherDashboard(teacherId));
    }
}
