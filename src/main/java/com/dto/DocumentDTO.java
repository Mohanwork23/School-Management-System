package com.dto;

import lombok.Data;
@Data
public class DocumentDTO {
    private Long id;
    private String name;
    private String fileType;
    private Long fileSize;
    private String fileName;
    private String associatedWith;
    private String uploadedBy;
    private String description;
    private String downloadUrl;

}
