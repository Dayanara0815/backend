package com.utp.adoptappbackend.pet.service.impl;

import com.utp.adoptappbackend.common.exception.ApiValidateException;
import com.utp.adoptappbackend.common.util.ConstantUtil;
import com.utp.adoptappbackend.pet.mapper.PetMapper;
import com.utp.adoptappbackend.pet.model.Pet;
import com.utp.adoptappbackend.pet.model.dto.PetRequest;
import com.utp.adoptappbackend.pet.model.dto.PetResponse;
import com.utp.adoptappbackend.pet.repository.PetRepository;
import com.utp.adoptappbackend.pet.service.PetService;
import com.utp.adoptappbackend.user.model.User;
import com.utp.adoptappbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> findAll() {
        return petRepository.findAll().stream()
                .map(petMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetResponse findById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));
        return petMapper.toResponse(pet);
    }

    @Override
    @Transactional
    public PetResponse create(PetRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiValidateException("Usuario no encontrado para crear la publicación."));
        
        Pet pet = petMapper.toEntity(request, user);
        return petMapper.toResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public PetResponse update(Long id, PetRequest request) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ApiValidateException(ConstantUtil.NOT_FOUND));

        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setAge(request.getAge());
        pet.setColor(request.getColor());
        pet.setSize(request.getSize());
        pet.setDescription(request.getDescription());
        pet.setImage(request.getImage());
        pet.setStatus(request.getStatus());

        return petMapper.toResponse(petRepository.save(pet));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!petRepository.existsById(id)) {
            throw new ApiValidateException(ConstantUtil.NOT_FOUND);
        }
        petRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PetResponse> findByUserId(Long userId) {
        return petRepository.findByUserId(userId).stream()
                .map(petMapper::toResponse)
                .collect(Collectors.toList());
    }
}
