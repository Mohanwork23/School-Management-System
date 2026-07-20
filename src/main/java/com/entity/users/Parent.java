package com.entity.users;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Parent extends User {

    @Column(unique = true, nullable = false)
    private String parentId; // e.g. PAR2025001

    @OneToMany(mappedBy = "parent")
    @JsonManagedReference("parent-children")
    private List<Student> children;
    private String address;
    private String gender;
	
}
