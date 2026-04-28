package com.crud_operation_backend.dto;

import lombok.Data;

@Data
public class AuthRequest {

    private String username;
    private String password;

    // OTP field added
    private String otp;
}