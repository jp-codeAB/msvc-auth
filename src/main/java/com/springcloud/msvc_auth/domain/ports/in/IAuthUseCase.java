package com.springcloud.msvc_auth.domain.ports.in;

import com.springcloud.msvc_auth.domain.model.User;
import java.util.Optional;

public interface IAuthUseCase {
    User registerUser(User user);
    Optional<String> login(String username, String rawPassword);
    User getAuthenticatedUser(String username);
}