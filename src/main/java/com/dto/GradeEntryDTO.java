package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GradeEntryDTO {
    private Long classId;
    private Long subjectId;
    private String examName;
    private List<MarkEntry> marks;

    @Getter
    @Setter
    public static class MarkEntry {
        private Long studentId;
        private Double marksObtained;
    }
}
