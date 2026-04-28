package com.crud_operation_backend.service;

import com.crud_operation_backend.dto.AuthRequest;
import com.crud_operation_backend.entity.UserEntity;
import com.crud_operation_backend.repository.UserRepository;
import com.crud_operation_backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    // Register API
    public String register(AuthRequest request) {

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        repo.save(user);

        return "User Registered Successfully";
    }

    // Login API
    public String login(AuthRequest request) {

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(request.getPassword(), user.getPassword())) {
            return "Login Successful";
        }

        throw new RuntimeException("Invalid credentials");
    }

    // Send OTP API
    public String sendOtp(AuthRequest request) {

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        repo.save(user);

        return "OTP Sent Successfully : " + otp;
    }

    // Verify OTP API
    public String verifyOtp(AuthRequest request) {

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp().equals(request.getOtp())) {

            user.setOtp(null); // clear OTP after verify
            repo.save(user);

            return jwtUtil.generateToken(user.getUsername());
        }

        throw new RuntimeException("Invalid OTP");
    }
}
