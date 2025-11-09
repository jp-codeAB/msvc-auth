package com.springcloud.msvc_auth.infrastructure.web.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TokenValidationResponse {
    private Long id;
    private String username;
    private String rol;
    private String email;
}