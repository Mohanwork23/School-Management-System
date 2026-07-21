package com.service;

import com.dto.ApiResponse;
import com.entity.users.User;

public interface NotificationService {
    void sendNotification(User recipient, String title, String message, String type);
    ApiResponse getNotifications(String username);
    ApiResponse markAllRead(String username);
}
