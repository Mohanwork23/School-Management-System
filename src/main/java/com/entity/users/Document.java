package com.entity.users;

import com.entity.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Document extends BaseEntity {

    private String name;               // e.g., "Aadhaar", "Photo"
    private String fileName;          // Original file name
    private String contentType;       // MIME type
    private long fileSize;            // bytes
    private String associatedWith;    // "STUDENT", "TEACHER", etc.
    private String description;       // Optional

    @Lob
    private byte[] fileContent;       // 👈 File stored as BLOB

    @ManyToOne
    @JoinColumn(name = "uploaded_by_id")
    private User uploadedBy;
}

