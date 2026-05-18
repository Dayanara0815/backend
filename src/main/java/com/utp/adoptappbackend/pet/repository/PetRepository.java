package com.utp.adoptappbackend.pet.repository;

import com.utp.adoptappbackend.common.model.enumeration.Status;
import com.utp.adoptappbackend.pet.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByStatusNot(Status status);
    List<Pet> findByUserIdAndStatusNot(Long userId, Status status);
}
