package com.dto;

import com.entity.attendance.AttendanceStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AttendanceDTO {
 private String date;
 private AttendanceStatus status;  
 private String remarks;
 private String studentName;
}
