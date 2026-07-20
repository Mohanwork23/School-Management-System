package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterStudentDTO {
    private String fullName;
    private String email;
    private String phone;
    private String gender;
    private String dob;
    private Long classRoomId;
    private String parentUsername; 
}
