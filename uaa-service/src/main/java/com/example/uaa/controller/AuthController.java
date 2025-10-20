package com.example.uaa.controller;

import com.example.uaa.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/auth/login")
    public Map<String, String> authenticate(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            String token = tokenProvider.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("type", "Bearer");
            response.put("username", userDetails.getUsername());
            response.put("roles", userDetails.getAuthorities().toString());

            return response;
        } catch (AuthenticationException e) {
            throw new RuntimeException("用户名或密码错误");
        }
    }

    @PostMapping("/auth/refresh")
    public Map<String, String> refreshToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        // 实现刷新token逻辑
        return null;
    }
}
