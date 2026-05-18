package com.utp.adoptappbackend.user.mapper;

import com.utp.adoptappbackend.user.model.User;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;
import com.utp.adoptappbackend.user.model.Hostel;
import com.utp.adoptappbackend.user.model.dto.HostelResponse;
import com.utp.adoptappbackend.user.model.dto.UserUpdateRequest;
import com.utp.adoptappbackend.user.model.dto.HostelRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hostel", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", expression = "java(true)")
    User toEntity(UserRequest request);

    UserResponse toResponse(User entity);

    HostelResponse toHostelResponse(Hostel entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "dni", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "hostel", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromDto(UserUpdateRequest request, @MappingTarget User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "isVerified", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateHostelFromDto(HostelRequest request, @MappingTarget Hostel entity);
}