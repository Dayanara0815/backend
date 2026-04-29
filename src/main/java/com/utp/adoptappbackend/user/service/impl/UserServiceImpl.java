package com.utp.adoptappbackend.user.service.impl;

import com.utp.adoptappbackend.common.exception.ApiValidateException;
import com.utp.adoptappbackend.common.util.ConstantUtil;
import com.utp.adoptappbackend.user.mapper.UserMapper;
import com.utp.adoptappbackend.user.model.User;
import com.utp.adoptappbackend.user.model.dto.AuthRequest;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;
import com.utp.adoptappbackend.user.repository.UserRepository;
import com.utp.adoptappbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiValidateException("El correo electrónico ya está registrado.");
        }
        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiValidateException("Correo o contraseña incorrectos."));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new ApiValidateException("Correo o contraseña incorrectos.");
        }

        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));
        return userMapper.toResponse(user);
    }
}
