package com.entity.attendance;

import java.time.LocalDate;

import com.entity.academic.ClassRoom;
import com.entity.academic.Subject;
import com.entity.base.BaseEntity;
import com.entity.users.Student;
import com.entity.users.Teacher;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Attendance extends BaseEntity {

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status; 
    private String remarks;


    @ManyToOne
    private Student student;

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private Teacher teacher;

    @ManyToOne
    private ClassRoom classRoom;
}
