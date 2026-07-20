package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimeTableDTO {

    private String dayOfWeek;
    private int periodNumber;
    private String startTime;
    private String endTime;
    private Long classRoomId;
    private Long subjectId;
    private Long teacherId;
}
