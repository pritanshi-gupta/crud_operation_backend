package com.crud_operation_backend.controller;

import com.crud_operation_backend.dto.AuthRequest;
import com.crud_operation_backend.entity.RefreshToken;
import com.crud_operation_backend.service.AuthService;
import com.crud_operation_backend.service.RefreshTokenService;
import com.crud_operation_backend.security.JwtUtil;   // ✅ FIXED IMPORT

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

    @Autowired
    private RefreshTokenService refreshService;

    @Autowired
    private JwtUtil jwtUtil;   // ✅ FIXED (JwtService → JwtUtil)

    // REGISTER API
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody AuthRequest request) {

        String message = service.register(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("message", message);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // LOGIN API
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthRequest request) {

        Map<String, String> tokens = service.login(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("accessToken", tokens.get("accessToken"));
        response.put("refreshToken", tokens.get("refreshToken"));

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

    // VERIFY OTP API
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOtp(@RequestBody AuthRequest request) {

        String token = service.verifyOtp(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // REFRESH TOKEN API
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@RequestParam String token) {

        RefreshToken rt = refreshService.verify(token);

        String newAccessToken = jwtUtil.generateToken(rt.getUser().getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("accessToken", newAccessToken);

        return ResponseEntity.ok(response);
    }
}