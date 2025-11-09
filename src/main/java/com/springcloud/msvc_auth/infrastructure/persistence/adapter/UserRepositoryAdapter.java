package com.springcloud.msvc_auth.infrastructure.persistence.adapter;

import com.springcloud.msvc_auth.domain.model.User;
import com.springcloud.msvc_auth.domain.ports.out.IUserRepositoryPort;
import com.springcloud.msvc_auth.infrastructure.mapper.IUserMapper;
import com.springcloud.msvc_auth.infrastructure.persistence.entity.UserEntity;
import com.springcloud.msvc_auth.infrastructure.persistence.repository.IUserSpringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements IUserRepositoryPort {

    private final IUserSpringRepository springRepository;
    private final IUserMapper userMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userMapper.toEntity(user);
        UserEntity savedEntity = springRepository.save(entity);
        return userMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return springRepository.findByUsername(username)
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springRepository.findByEmail(email)
                .map(userMapper::toDomain);
    }
}