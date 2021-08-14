package com.petfabula.domain.aggregate.pet.respository;

import com.petfabula.domain.aggregate.pet.entity.PetBreed;

import java.util.List;

public interface PetBreedRepository {

    PetBreed save(PetBreed petBreed);

    List<PetBreed> findAll();

    List<PetBreed> findByIds(List<Long> ids);

    PetBreed findById(Long id);

    PetBreed findByCategoryIdName(Long categoryId, String name);

}
