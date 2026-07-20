package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.entity.academic.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long>{

	Optional<Subject> findByName(String name);

}
