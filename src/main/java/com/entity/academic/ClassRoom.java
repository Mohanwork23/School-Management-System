package com.entity.academic;

import java.util.ArrayList;
import java.util.List;

import com.entity.base.BaseEntity;
import com.entity.users.Student;
import com.entity.users.Teacher;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ClassRoom extends BaseEntity {

    @Column(nullable = false)
    private String className;

    private String section;

    @OneToMany(mappedBy = "classRoom")
    @JsonManagedReference("class-students")
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "assignedClasses")
    @JsonIgnore
    private List<Teacher> teachers = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "class_subject",
        joinColumns = @JoinColumn(name = "class_id"),
        inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @JsonIgnore 
    private List<Subject> subjects = new ArrayList<>();
}
