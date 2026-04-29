package com.utp.adoptappbackend.pet.expose;

import com.utp.adoptappbackend.common.model.ApiResponse;
import com.utp.adoptappbackend.common.util.ConstantUtil;
import com.utp.adoptappbackend.pet.model.dto.PetRequest;
import com.utp.adoptappbackend.pet.model.dto.PetResponse;
import com.utp.adoptappbackend.pet.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ApiResponse<List<PetResponse>> findAll() {
        return ApiResponse.<List<PetResponse>>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(petService.findAll())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<PetResponse> findById(@PathVariable Long id) {
        return ApiResponse.<PetResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(petService.findById(id))
                .build();
    }

    @PostMapping
    public ApiResponse<PetResponse> create(@Valid @RequestBody PetRequest request) {
        return ApiResponse.<PetResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(petService.create(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<PetResponse> update(@PathVariable Long id, @Valid @RequestBody PetRequest request) {
        return ApiResponse.<PetResponse>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(petService.update(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable Long id) {
        petService.delete(id);
        return ApiResponse.<Void>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.DELETED)
                .build();
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<PetResponse>> findByUserId(@PathVariable Long userId) {
        return ApiResponse.<List<PetResponse>>builder()
                .code(ConstantUtil.OK_CODE)
                .message(ConstantUtil.OK_MESSAGE)
                .data(petService.findByUserId(userId))
                .build();
    }
}
