package com.nneomablessyn.packagefinder.usermanagement.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserRequestDto {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String fullName;

    @NotBlank
    private String password;

}
