package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AttendanceMarkDTO {
    private Long classId;
    private String date; 
    private List<AttendanceEntry> entries;

    @Getter
    @Setter
    public static class AttendanceEntry {
        private String studentId;
        private String status; 
    }
}
