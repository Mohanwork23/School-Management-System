package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.academic.ClassRoom;

public interface ClassRoomRepository extends JpaRepository<ClassRoom, Long> {
	long count();
}
