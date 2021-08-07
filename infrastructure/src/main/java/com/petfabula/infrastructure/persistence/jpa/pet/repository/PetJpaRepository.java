package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetJpaRepository extends JpaRepository<Pet, Long> {

    List<Pet> findByFeederId(Long id);
}
