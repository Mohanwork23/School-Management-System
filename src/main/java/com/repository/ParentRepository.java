package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.entity.users.Parent;

public interface ParentRepository extends JpaRepository<Parent, Long> {
    Optional<Parent> findByParentId(String parentId);
    Optional<Parent> findByUsername(String username);
}
