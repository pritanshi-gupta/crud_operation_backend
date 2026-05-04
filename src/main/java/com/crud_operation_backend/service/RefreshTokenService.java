package com.crud_operation_backend.service;

import com.crud_operation_backend.entity.RefreshToken;
import com.crud_operation_backend.entity.UserEntity;
import com.crud_operation_backend.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repo;

    // Create Refresh Token
    public String createToken(UserEntity user) {

        RefreshToken token = new RefreshToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(7));

        repo.save(token);

        return token.getToken();
    }

    // Verify Token
    public RefreshToken verify(String token) {

        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        if (rt.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh Token Expired");
        }

        return rt;
    }
}
