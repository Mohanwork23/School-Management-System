package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimetableSlotDTO {
    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private String subjectName;
    private String teacherName;
}
