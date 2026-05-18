package com.utp.adoptappbackend.user.service.impl;

import com.utp.adoptappbackend.common.exception.ApiValidateException;
import com.utp.adoptappbackend.common.model.PageResponse;
import com.utp.adoptappbackend.common.model.enumeration.Role;
import com.utp.adoptappbackend.common.util.ConstantUtil;
import com.utp.adoptappbackend.common.util.TokenUtil;
import com.utp.adoptappbackend.shared.client.AuthClient;
import com.utp.adoptappbackend.user.mapper.UserMapper;
import com.utp.adoptappbackend.user.model.Hostel;
import com.utp.adoptappbackend.user.model.User;
import com.utp.adoptappbackend.user.model.dto.AuthRegisterRequest;
import com.utp.adoptappbackend.user.model.dto.LoginResponse;
import com.utp.adoptappbackend.user.model.dto.TokenClientResponse;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;
import com.utp.adoptappbackend.user.model.dto.UserUpdateRequest;
import com.utp.adoptappbackend.user.repository.UserRepository;
import com.utp.adoptappbackend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AuthClient authClient;


    @Override
    @Transactional
    public UserResponse register(UserRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ApiValidateException("El correo electrónico ya está registrado.");
        }

        // 1. Generar un externalId UUID aleatorio (requerido por ms-auth-service-helper)
        UUID externalId = UUID.randomUUID();

        // 2. Registrar credenciales en el microservicio de autenticación auxiliar
        AuthRegisterRequest authReq = AuthRegisterRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole().name())
                .externalId(externalId)
                .build();
        authClient.register(authReq);

        // 3. Guardar el usuario en la base de datos local de negocio
        User user = userMapper.toEntity(request);

        // Si es un Hostel, instanciamos la relación bidireccional
        if (request.getRole() == Role.HOSTEL && request.getHostel() != null) {
            Hostel hostel = Hostel.builder()
                    .user(user)
                    .hostelName(request.getHostel().getHostelName())
                    .description(request.getHostel().getDescription())
                    .capacity(request.getHostel().getCapacity())
                    .logo(request.getHostel().getLogo())
                    .donationLink(request.getHostel().getDonationLink())
                    .website(request.getHostel().getWebsite())
                    .facebookUrl(request.getHostel().getFacebookUrl())
                    .instagramUrl(request.getHostel().getInstagramUrl())
                    .isVerified(false)
                    .build();
            user.setHostel(hostel);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(AuthRegisterRequest request) {
        // 1. Validar inicio de sesión en el servicio de autenticación
        TokenClientResponse token = authClient.login(request);

        // 2. Obtener los detalles del perfil desde el repositorio local
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ApiValidateException("Usuario no encontrado en la base de datos de negocio."));

        // 3. Validar que la cuenta esté activa
        if (!user.getIsActive()) {
            throw new ApiValidateException("La cuenta ha sido desactivada. Contacta con soporte para más información.");
        }

        // 4. Retornar el response unificado
        return LoginResponse.builder()
                .tokenType(token.getTokenType())
                .accessToken(token.getAccessToken())
                .expiresIn(token.getExpiresIn())
                .refreshToken(token.getRefreshToken())
                .user(userMapper.toResponse(user))
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException("Usuario no encontrado para actualización."));

        // MapStruct actualiza de forma automática los campos principales no nulos de User
        userMapper.updateUserFromDto(request, user);

        // Si es un Hostel, delegamos a MapStruct la actualización de la relación anidada
        if (user.getRole() == Role.HOSTEL && request.getHostel() != null) {
            Hostel hostel = user.getHostel();
            if (hostel == null) {
                hostel = new Hostel();
                hostel.setUser(user);
                user.setHostel(hostel);
            }
            // MapStruct actualiza automáticamente todos los campos no nulos de Hostel
            userMapper.updateHostelFromDto(request.getHostel(), hostel);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> pageUsers = userRepository.findAll(pageable);

        return new PageResponse<>(
                pageUsers.getContent().stream()
                        .map(userMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                pageUsers.getTotalElements(),
                pageUsers.getTotalPages()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserResponse> findByRole(Role role, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> pageUsers = userRepository.findByRole(role, pageable);

        return new PageResponse<>(
                pageUsers.getContent().stream()
                        .map(userMapper::toResponse)
                        .collect(Collectors.toList()),
                page,
                size,
                pageUsers.getTotalElements(),
                pageUsers.getTotalPages()
        );
    }

    @Override
    @Transactional
    public UserResponse deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));

        if (!user.getIsActive()) {
            throw new ApiValidateException("La cuenta ya está desactivada.");
        }

        user.setIsActive(false);

        // Si el usuario es un albergue, desactiva también el albergue
        if (user.getRole() == Role.HOSTEL && user.getHostel() != null) {
            user.getHostel().setIsActive(false);
        }

        User savedUser = userRepository.save(user);
        log.info("Cuenta desactivada para el usuario: {}", user.getEmail());

        return userMapper.toResponse(savedUser);
    }

    @Override
    @Transactional
    public UserResponse activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));

        if (user.getIsActive()) {
            throw new ApiValidateException("La cuenta ya está activa.");
        }

        user.setIsActive(true);

        // Si el usuario es un albergue, activa también el albergue
        if (user.getRole() == Role.HOSTEL && user.getHostel() != null) {
            user.getHostel().setIsActive(true);
        }

        User savedUser = userRepository.save(user);
        log.info("Cuenta activada para el usuario: {}", user.getEmail());

        return userMapper.toResponse(savedUser);
    }
}