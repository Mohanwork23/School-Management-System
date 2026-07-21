package com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ApiResponse;
import com.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getNotifications(@PathVariable String username) {
        return ResponseEntity.ok(notificationService.getNotifications(username));
    }

    @PutMapping("/{username}/mark-read")
    public ResponseEntity<ApiResponse> markAllRead(@PathVariable String username) {
        return ResponseEntity.ok(notificationService.markAllRead(username));
    }
}
