package com.springcloud.msvc_auth.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
public class LoginRequest {

    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
