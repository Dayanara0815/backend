package com.utp.adoptappbackend.pet.service;

import com.utp.adoptappbackend.pet.model.dto.PetRequest;
import com.utp.adoptappbackend.pet.model.dto.PetResponse;

import java.util.List;

public interface PetService {
    List<PetResponse> findAll();
    PetResponse findById(Long id);
    PetResponse create(PetRequest request);
    PetResponse update(Long id, PetRequest request);
    void delete(Long id);
    List<PetResponse> findByUserId(Long userId);
}
