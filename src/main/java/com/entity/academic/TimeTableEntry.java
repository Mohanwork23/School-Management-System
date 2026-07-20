package com.entity.academic;

import com.entity.users.Teacher;
import com.entity.base.BaseEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class TimeTableEntry extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "classroom_id")
    private ClassRoom classRoom;

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; 

    @Column(nullable = false)
    private String period; 

    @Column(nullable = false)
    private LocalTime startTime;
    
    @Column(nullable = false)
    private LocalTime endTime;

    private String remarks; 
}
