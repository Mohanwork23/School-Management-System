package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.notification.Notification;
import com.entity.users.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipientOrderByCreatedAtDesc(User recipient);
    List<Notification> findByRecipientAndIsReadFalse(User recipient);
}
