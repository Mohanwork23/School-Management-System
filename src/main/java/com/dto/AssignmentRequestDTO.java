package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssignmentRequestDTO {
    private String title;
    private String description;
    private String fileUrl;         
    private LocalDate dueDate;
    private Long classId;
    private Long subjectId;
}
