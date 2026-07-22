package com.dto;

import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterTeacherDTO {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Qualification is required")
    private String qualification;

    private List<Long> subjectIds;
    private List<Long> classIds;
}
