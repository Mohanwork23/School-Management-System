package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ApiResponse;
import com.service.ParentService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Parent", description = "Parent portal - view child grades, attendance, fees and results")
@RestController
@RequestMapping("/api/parent")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ParentController {

    private final ParentService parentService;

    @GetMapping("/{parentId}/grades")
    public ResponseEntity<ApiResponse> getChildGrades(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildGrades(parentId));
    }

    @GetMapping("/{parentId}/attendance")
    public ResponseEntity<ApiResponse> getChildAttendance(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildAttendance(parentId));
    }

    @Operation(summary = "Child fee status", description = "Returns fee payment history for all children of this parent")
    @GetMapping("/{parentId}/fees")
    public ResponseEntity<ApiResponse> getChildFeeStatus(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildFeeStatus(parentId));
    }

    @GetMapping("/{parentId}/results")
    public ResponseEntity<ApiResponse> getChildResults(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildResults(parentId));
    }

    @Operation(summary = "Parent dashboard", description = "Returns summary of all children linked to this parent")
    @GetMapping("/{parentId}/dashboard")
    public ResponseEntity<ApiResponse> getParentDashboard(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getParentDashboard(parentId));
    }

    @GetMapping("/{parentId}/profile")
    public ResponseEntity<ApiResponse> getParentProfile(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getParentProfile(parentId));
    }

    @Operation(summary = "Child timetable", description = "Returns timetable for all children of this parent")
    @GetMapping("/{parentId}/timetable")
    public ResponseEntity<ApiResponse> getChildTimetable(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildTimetable(parentId));
    }

    @Operation(summary = "Child upcoming assignments", description = "Returns upcoming assignment deadlines for all children")
    @GetMapping("/{parentId}/upcoming-assignments")
    public ResponseEntity<ApiResponse> getChildUpcomingAssignments(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildUpcomingAssignments(parentId));
    }
}
