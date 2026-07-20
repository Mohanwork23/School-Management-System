package com.entity.academic;

import java.time.LocalDateTime;

import com.entity.base.BaseEntity;
import com.entity.users.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Assignment extends BaseEntity {

    private String title;

    private String description;

    private String fileUrl; 

    private LocalDateTime assignedAt = LocalDateTime.now();

    private LocalDateTime dueDate;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private ClassRoom classRoom;

    @ManyToOne
    private Subject subject;
}
