package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PetJpaRepository extends JpaRepository<Pet, Long> {

    @Query("select p from Pet p where p.id = :id")
    Optional<Pet> findById(Long id);

    List<Pet> findByFeederId(Long id);

    Pet findByName(String name);
}
