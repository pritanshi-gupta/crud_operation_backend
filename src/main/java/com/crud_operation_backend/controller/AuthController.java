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

    // REGISTER API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AuthRequest request) {

        String message = service.register(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // LOGIN API (✔ UPDATED - now returns JWT)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request) {

        String token = service.login(request);  // 🔥 now returns JWT

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("token", token);   // ✔ frontend ke liye important

        return ResponseEntity.ok(response);
    }

    // SEND OTP API
    @PostMapping("/resend-otp")
    public ResponseEntity<Map<String, Object>> sendOtp(@RequestBody AuthRequest request) {

        String message = service.sendOtp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return ResponseEntity.ok(response);
    }

    // VERIFY OTP API (✔ already correct)
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody AuthRequest request) {

        String token = service.verifyOtp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}