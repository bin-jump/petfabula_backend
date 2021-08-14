package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetBreedJpaRepository extends JpaRepository<PetBreed, Long> {

    PetBreed findByCategoryIdAndName(Long breedId, String name);

    List<PetBreed> findByIdIn(List<Long> ids);
}
