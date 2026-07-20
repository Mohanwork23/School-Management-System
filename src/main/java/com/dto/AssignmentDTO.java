package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentDTO {
    private Long id;
    private String title;
    private String description;
    private String dueDate;
    private String subjectName;
    private String fileUrl;
}
