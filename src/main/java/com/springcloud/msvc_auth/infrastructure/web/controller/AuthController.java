package com.springcloud.msvc_auth.infrastructure.web.controller;

import com.springcloud.msvc_auth.infrastructure.web.dto.TokenValidationResponse;
import com.springcloud.msvc_auth.shared.exception.BusinessException;
import com.springcloud.msvc_auth.infrastructure.auth.util.JwtUtil;
import com.springcloud.msvc_auth.domain.model.User;
import com.springcloud.msvc_auth.domain.ports.in.IAuthUseCase;
import com.springcloud.msvc_auth.infrastructure.web.dto.AuthResponse;
import com.springcloud.msvc_auth.infrastructure.web.dto.LoginRequest;
import com.springcloud.msvc_auth.infrastructure.web.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthUseCase authUseCase;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User userToRegister = User.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();
        authUseCase.registerUser(userToRegister);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authUseCase.login(request.getUsername(), request.getPassword())
                .orElseThrow(() -> new BusinessException("Invalid credentials"));
        User user = authUseCase.getAuthenticatedUser(request.getUsername());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .username(user.getUsername())
                .rol(user.getRol().name())
                .build());
    }

    @GetMapping("/validate-token")
    public ResponseEntity<TokenValidationResponse> validateToken(@RequestParam String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = authUseCase.getAuthenticatedUser(username);
        TokenValidationResponse response = TokenValidationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .rol(user.getRol().name())
                .email(user.getEmail())
                .build();
        return ResponseEntity.ok(response);
    }
}