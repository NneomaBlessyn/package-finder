package com.nneomablessyn.packagefinder.usermanagement.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
