package com.utp.adoptappbackend.user.expose;

import com.utp.adoptappbackend.common.model.ApiResponse;
import com.utp.adoptappbackend.common.util.ConstantUtil;
import com.utp.adoptappbackend.user.model.dto.AuthRequest;
import com.utp.adoptappbackend.user.model.dto.UserRequest;
import com.utp.adoptappbackend.user.model.dto.UserResponse;
import com.utp.adoptappbackend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(userService.register(request))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<UserResponse> login(@Valid @RequestBody AuthRequest request) {
        return ApiResponse.<UserResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(userService.login(request))
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> findById(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(userService.findById(id))
                .build();
    }
}
