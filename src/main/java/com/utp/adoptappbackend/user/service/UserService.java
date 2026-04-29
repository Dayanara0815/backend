package com.utp.adoptappbackend.user.service;

import com.utp.adoptappbackend.user.model.dto.AuthRequest;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;

public interface UserService {
    UserResponse register(UserRequest request);
    UserResponse login(AuthRequest request);
    UserResponse findById(Long id);
}
