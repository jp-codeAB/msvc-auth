package com.springcloud.msvc_auth.domain.ports.out;

import com.springcloud.msvc_auth.domain.model.User;
import java.util.Optional;

public interface IUserRepositoryPort {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
