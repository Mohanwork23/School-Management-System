package com.entity.academic;

import com.entity.base.BaseEntity;
import com.entity.users.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Result extends BaseEntity {

    @ManyToOne
    private Student student;

    @ManyToOne
    private Exam exam;

    private Double marksObtained;

    private String grade;          

    private String remarks;      
}
