package com.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterTeacherDTO {
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String qualification;
    private List<Long> subjectIds;
    private List<Long> classIds;
}
