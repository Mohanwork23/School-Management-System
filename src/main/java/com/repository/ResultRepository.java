package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.Result;
import com.entity.users.Student;

public interface ResultRepository extends JpaRepository<Result, Long>{

	List<Result> findByStudent(Student student);

}
