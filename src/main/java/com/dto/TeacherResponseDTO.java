package com.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String qualification;
    private String teacherId;
    private List<SubjectDTO> subjects;
    private List<ClassRoomDTO> assignedClasses;
}
