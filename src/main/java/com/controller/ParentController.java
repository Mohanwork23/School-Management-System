package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ApiResponse;
import com.service.ParentService;

import lombok.RequiredArgsConstructor;

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

//    @GetMapping("/{parentId}/fees")
//    public ResponseEntity<ApiResponse> getChildFeeStatus(@PathVariable String parentId) {
//        return ResponseEntity.ok(parentService.getChildFeeStatus(parentId));
//    }

    @GetMapping("/{parentId}/results")
    public ResponseEntity<ApiResponse> getChildResults(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getChildResults(parentId));
    }

    @GetMapping("/{parentId}/dashboard")
    public ResponseEntity<ApiResponse> getParentDashboard(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getParentDashboard(parentId));
    }

    @GetMapping("/{parentId}/profile")
    public ResponseEntity<ApiResponse> getParentProfile(@PathVariable String parentId) {
        return ResponseEntity.ok(parentService.getParentProfile(parentId));
    }

    // For Razorpay payment integration:
    // @PostMapping("/{parentId}/pay-fee")
    // public ResponseEntity<ApiResponse> payFees(...) { ... }
}
