package com.springcloud.msvc_auth.infrastructure.mapper;

import com.springcloud.msvc_auth.domain.model.User;
import com.springcloud.msvc_auth.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IUserMapper {
    UserEntity toEntity(User domain);
    User toDomain(UserEntity entity);
}