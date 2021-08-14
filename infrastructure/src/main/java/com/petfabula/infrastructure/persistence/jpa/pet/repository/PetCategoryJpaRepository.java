package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetCategoryJpaRepository extends JpaRepository<PetCategory, Long> {

    PetCategory findByName(String name);
}
