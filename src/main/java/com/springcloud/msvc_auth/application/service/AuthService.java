package com.springcloud.msvc_auth.application.service;

import com.springcloud.msvc_auth.domain.model.Role;
import com.springcloud.msvc_auth.domain.model.User;
import com.springcloud.msvc_auth.domain.ports.in.IAuthUseCase;
import com.springcloud.msvc_auth.domain.ports.out.IUserRepositoryPort;
import com.springcloud.msvc_auth.shared.exception.BusinessException;
import com.springcloud.msvc_auth.infrastructure.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthUseCase {

    private final IUserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public User registerUser(User user) {

        if (userRepositoryPort.findByUsername(user.getUsername()).isPresent()) {
            throw new BusinessException("The username is already registered");
        }

        if (userRepositoryPort.findByEmail(user.getEmail()).isPresent()) {
            throw new BusinessException("The email address is already registered");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Role defaultRole = Role.CLIENT;

        User userToSave = User.builder()
                .username(user.getUsername())
                .password(encodedPassword)
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .rol(defaultRole)
                .build();

        return userRepositoryPort.save(userToSave);
    }

    @Override
    public Optional<String> login(String username, String rawPassword) {
        Optional<User> userOpt = userRepositoryPort.findByUsername(username);

        if (userOpt.isEmpty() || !passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
            return Optional.empty();
        }
        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRol().name());

        return Optional.of(token);
    }


    @Override
    public User getAuthenticatedUser(String username) {
        return userRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new BusinessException("User not found"));
    }
}