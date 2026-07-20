package com.entity.academic;

import java.time.LocalDateTime;

import com.entity.base.BaseEntity;
import com.entity.users.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssignmentSubmission extends BaseEntity {

    @ManyToOne
    private Assignment assignment;

    @ManyToOne
    private Student student;

    private String fileUrl;             

    private String comments;           

    private LocalDateTime submittedAt = LocalDateTime.now();
}
