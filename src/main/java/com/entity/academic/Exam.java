package com.entity.academic;

import java.time.LocalDate;

import com.entity.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Exam extends BaseEntity {

    private String title;       

    private LocalDate examDate;

    private String term;           

    private String academicYear;   

    @ManyToOne
    private Subject subject;

    @ManyToOne
    private ClassRoom classRoom;
}
