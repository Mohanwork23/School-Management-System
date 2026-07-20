package com.entity.fees;

import com.entity.academic.ClassRoom;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FeeStructure {

    @Id @GeneratedValue
    private Long id;

    private String academicYear;

    @ManyToOne
    private ClassRoom classRoom;

    @ManyToOne
    private FeeComponent component; 

    private Double amount;

    private String description;
}
