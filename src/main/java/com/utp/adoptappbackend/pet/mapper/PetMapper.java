package com.utp.adoptappbackend.pet.mapper;

import com.utp.adoptappbackend.pet.model.Pet;
import com.utp.adoptappbackend.pet.model.dto.PetRequest;
import com.utp.adoptappbackend.pet.model.dto.PetResponse;
import com.utp.adoptappbackend.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    Pet toEntity(PetRequest request, User user);

    @Mapping(target = "userId", source = "user.id")
    PetResponse toResponse(Pet entity);
}
