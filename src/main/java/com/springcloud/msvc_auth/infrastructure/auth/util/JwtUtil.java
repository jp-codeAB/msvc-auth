package com.springcloud.msvc_auth.infrastructure.auth.util;

import com.springcloud.msvc_auth.shared.exception.BusinessException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String rol) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("rol", rol);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public Jws<Claims> validateTokenAndGetClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
    }

    public String getUsernameFromToken(String token) {
        try {
            return validateTokenAndGetClaims(token).getBody().getSubject();
        } catch (JwtException e) {
            throw new BusinessException("Invalid or expired token: " + e.getMessage());
        }
    }

    public String getRoleFromToken(String token) {
        try {
            return validateTokenAndGetClaims(token).getBody().get("rol", String.class);
        } catch (JwtException e) {
            throw new BusinessException("Invalid or expired token: " + e.getMessage());
        }
    }
}