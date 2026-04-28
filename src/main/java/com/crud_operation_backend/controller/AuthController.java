package com.crud_operation_backend.controller;

import com.crud_operation_backend.dto.AuthRequest;
import com.crud_operation_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AuthRequest request) {

        String message = service.register(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request) {

        String message = service.login(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    // Send OTP API
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody AuthRequest request) {

        String message = service.sendOtp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    // Verify OTP API
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody AuthRequest request) {

        String token = service.verifyOtp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}