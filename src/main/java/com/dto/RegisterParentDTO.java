package com.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterParentDTO {
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
}
