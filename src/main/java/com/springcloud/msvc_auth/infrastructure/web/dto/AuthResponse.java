package com.springcloud.msvc_auth.infrastructure.web.dto;

import lombok.*;

@Getter
@Builder
public class AuthResponse {
    private String token;
    private String username;
    private String rol;
}