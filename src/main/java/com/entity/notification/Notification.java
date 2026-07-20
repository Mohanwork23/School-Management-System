package com.entity.notification;

import com.entity.base.BaseEntity;
import com.entity.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User recipient;

    private String title;

    @Column(length = 1000)
    private String message;

    private boolean isRead = false;

    private String type; // e.g. FEE, ATTENDANCE, RESULT, ASSIGNMENT
}
