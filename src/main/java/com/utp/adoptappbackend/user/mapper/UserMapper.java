package com.utp.adoptappbackend.user.mapper;

import com.utp.adoptappbackend.user.model.User;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserRequest request);

    UserResponse toResponse(User entity);
}
