package com.dto.exam;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ExamCreateDTO {
    @NotBlank(message = "Exam title is required")
    private String title;

    @NotNull(message = "Exam date is required")
    @FutureOrPresent(message = "Exam date must not be in the past")
    private LocalDate examDate;

    @NotBlank(message = "Term is required")
    private String term;

    @NotBlank(message = "Academic year is required")
    private String academicYear;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Class ID is required")
    private Long classId;
}