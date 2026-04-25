package com.crud_operation_backend.controller;


import com.crud_operation_backend.dto.AuthRequest;
import com.crud_operation_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public String register (@RequestBody AuthRequest request){
        return service.register(request);

    }

    @PostMapping("/login")
    public String login(@RequestBody AuthRequest request){
        return service.login(request);
    }
}
