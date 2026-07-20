package com.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StudentResponseDTO {
 
	private Long id;
    private String studentId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private boolean active;
    private String dateOfBirth;
    private String gender;
    private String parentUsername; 
    private Long classRoomId; 
    private String className; 
    private String section; 
    private List<String> subjects; 

    
}
