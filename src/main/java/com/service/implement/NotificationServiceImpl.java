package com.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;

import com.dto.ApiResponse;
import com.entity.notification.Notification;
import com.entity.users.User;
import com.repository.NotificationRepository;
import com.repository.UserRepository;
import com.service.NotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    public void sendNotification(User recipient, String title, String message, String type) {
        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notificationRepository.save(notification);
    }

    @Override
    public ApiResponse getNotifications(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        List<Notification> notifications = notificationRepository.findByRecipientOrderByCreatedAtDesc(user);
        return new ApiResponse("Notifications fetched", true, notifications);
    }

    @Override
    public ApiResponse markAllRead(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        List<Notification> unread = notificationRepository.findByRecipientAndIsReadFalse(user);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        return new ApiResponse("All notifications marked as read", true);
    }
}
