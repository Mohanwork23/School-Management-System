package com.entity.users;

import java.util.List;

import com.entity.academic.ClassRoom;
import com.entity.academic.Subject;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Teacher extends User {

    @Column(unique = true, nullable = false)
    private String teacherId;

    private String qualification;
    private String department;

    @ManyToMany
    private List<Subject> subjects;

    @ManyToMany
    @JsonBackReference // ✅ If you want to map @JsonManagedReference from ClassRoom, use an identifier (optional)
    private List<ClassRoom> assignedClasses;
}
