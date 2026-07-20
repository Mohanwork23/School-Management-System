package com.entity.fees;

import com.entity.users.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Student student;

    @ManyToOne
    private FeeStructure feeStructure;

    private LocalDate paymentDate;
    private String modeOfPayment; 
    private Double amountPaid;
}
