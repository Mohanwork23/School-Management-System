package com.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TeacherDashboardDTO {
    private List<String> assignedSubjects;
    private List<String> assignedClasses;
    private int totalAssignmentsPosted;
    private int totalAttendanceMarked;
}
