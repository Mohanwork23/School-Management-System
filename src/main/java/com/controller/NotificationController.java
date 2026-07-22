package com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dto.ApiResponse;
import com.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Tag(name = "Notifications", description = "Get and manage user notifications")
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @Operation(summary = "Get notifications", description = "Returns all notifications for the given username")
    @GetMapping("/{username}")
    public ResponseEntity<ApiResponse> getNotifications(@PathVariable String username) {
        return ResponseEntity.ok(notificationService.getNotifications(username));
    }

    @Operation(summary = "Mark all read", description = "Marks all unread notifications as read for the given username")
    @PutMapping("/{username}/mark-read")
    public ResponseEntity<ApiResponse> markAllRead(@PathVariable String username) {
        return ResponseEntity.ok(notificationService.markAllRead(username));
    }
}
