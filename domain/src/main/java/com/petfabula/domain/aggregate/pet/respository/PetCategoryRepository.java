package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.PetCategory;

import java.util.List;

public interface PetCategoryRepository {

    PetCategory save(PetCategory petCategory);

    PetCategory findById(Long id);

    PetCategory findByName(String name);

    List<PetCategory> findAll();
}
