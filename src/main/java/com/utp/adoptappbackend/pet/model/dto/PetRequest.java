package com.utp.adoptappbackend.pet.model.dto;

import com.utp.adoptappbackend.common.model.enumeration.Size;
import com.utp.adoptappbackend.common.model.enumeration.Species;
import com.utp.adoptappbackend.common.model.enumeration.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetRequest {

    @NotBlank(message = "el nombre es obligatorio")
    private String name;

    @NotNull(message = "la especie es obligatoria")
    private Species species;

    private String age;
    private String color;
    private Size size;
    private String description;
    private String image;

    @NotNull(message = "el estado es obligatorio")
    private Status status;

    @NotNull(message = "el id de usuario es obligatorio")
    private Long userId;
}
