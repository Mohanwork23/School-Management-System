package com.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultEntryDTO {
    private Long studentId;
    private Long examId;
    private Double marksObtained;
    private String grade;
    private String remarks;
}
