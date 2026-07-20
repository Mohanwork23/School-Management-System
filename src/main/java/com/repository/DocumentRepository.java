package com.repository;

import com.entity.users.Document;
import com.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByUploadedBy(User uploadedBy);            // for fetching user-specific documents
    List<Document> findByAssociatedWith(String associatedWith);  // if needed for filtering STUDENT/TEACHER
}
