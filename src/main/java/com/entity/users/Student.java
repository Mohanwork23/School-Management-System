package com.entity.users;

import java.time.LocalDate;

import com.entity.academic.ClassRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Student extends User {

    @Column(unique = true, nullable = false)
    private String studentId; // e.g. STU2025001

    private LocalDate dateOfBirth;
    private String gender;

    @ManyToOne
    @JsonBackReference("class-students") // ✅ Matches ClassRoom @JsonManagedReference("class-students")
    private ClassRoom classRoom;

    @ManyToOne
    @JsonBackReference("parent-children") // ✅ Used if Parent has @JsonManagedReference("parent-children")
    private Parent parent;
}
