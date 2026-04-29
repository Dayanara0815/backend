package com.utp.adoptappbackend.pet.model.dto;

import com.utp.adoptappbackend.common.model.enumeration.Size;
import com.utp.adoptappbackend.common.model.enumeration.Species;
import com.utp.adoptappbackend.common.model.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PetResponse {
    private Long id;
    private String name;
    private Species species;
    private String age;
    private String color;
    private Size size;
    private String description;
    private String image;
    private Status status;
    private Long userId;
}
