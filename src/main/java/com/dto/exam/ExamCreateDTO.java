package com.dto.exam;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ExamCreateDTO {
    private String title;
    private LocalDate examDate;
    private String term;
    private String academicYear;
    private Long subjectId;
    private Long classId;
}
