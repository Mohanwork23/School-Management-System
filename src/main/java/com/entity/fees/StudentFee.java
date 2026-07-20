package com.entity.fees;

import java.time.LocalDate;

import com.entity.users.Student;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class StudentFee {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private FeeStructure fee;   

    private LocalDate dueDate;
    private boolean paid;
    private LocalDate paymentDate;
    private String paymentMode;     
}
