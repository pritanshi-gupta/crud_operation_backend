package com.crud_operation_backend.service;

import com.crud_operation_backend.dto.AuthRequest;
import com.crud_operation_backend.entity.UserEntity;
import com.crud_operation_backend.repository.UserRepository;
import com.crud_operation_backend.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RefreshTokenService refreshTokenService;

    // REGISTER API
    public String register(AuthRequest request) {

        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));

        repo.save(user);

        return "User Registered Successfully";
    }

    // LOGIN API
    public Map<String, String> login(AuthRequest request) {

        SecureRandom secureRandom = new SecureRandom();
        int number = secureRandom.nextInt(1000000);
        String otp = String.format("%06d", number);

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (encoder.matches(request.getPassword(), user.getPassword())) {

            user.setOtp(otp);
            repo.save(user);

            String accessToken = jwtUtil.generateToken(user.getUsername());
            String refreshToken = refreshTokenService.createToken(user);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return tokens;
        }

        throw new RuntimeException("Invalid credentials");
    }

    // SEND OTP API
    public String sendOtp(AuthRequest request) {

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setOtp(otp);
        repo.save(user);

        return "OTP Sent Successfully";
    }

    // VERIFY OTP API
    public String verifyOtp(AuthRequest request) {

        UserEntity user = repo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp() != null && user.getOtp().equals(request.getOtp())) {

            user.setOtp(null);
            repo.save(user);

            return jwtUtil.generateToken(user.getUsername());
        }

        throw new RuntimeException("Invalid OTP");
    }

    // 🔥 FORGOT PASSWORD
    public String forgotPassword(String username) {

        UserEntity user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

        user.setResetOtp(otp);
        user.setOtpExpiry(java.time.LocalDateTime.now().plusMinutes(5));

        repo.save(user);

        return "OTP sent: " + otp;
    }

    // 🔥 RESET PASSWORD
    public String resetPassword(String username, String otp, String newPassword) {

        UserEntity user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getResetOtp() == null || !user.getResetOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setPassword(encoder.encode(newPassword));
        user.setResetOtp(null);

        repo.save(user);

        return "Password updated successfully";
    }
}