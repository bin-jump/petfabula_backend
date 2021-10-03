package com.petfabula.domain.aggregate.pet.service;

import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import com.petfabula.domain.aggregate.pet.respository.PetBreedRepository;
import com.petfabula.domain.aggregate.pet.respository.PetCategoryRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetBreedService {

    @Autowired
    private PetBreedRepository petBreedRepository;

    @Autowired
    private PetCategoryRepository petCategoryRepository;

    @Autowired
    private PetIdGenerator idGenerator;

    public PetBreed create(Long categoryId, String name) {
        PetBreed breed = petBreedRepository.findByCategoryIdName(categoryId, name);
        if (breed != null) {
            throw new InvalidOperationException(CommonMessageKeys.NAME_ALREADY_EXISTS);
        }

        PetCategory category = petCategoryRepository.findById(categoryId);
        if (category == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        Long id = idGenerator.nextId();
        breed = new PetBreed(id, categoryId, category.getName(), name);

        return petBreedRepository.save(breed);
    }

    public PetBreed update(Long breedId, String name) {
        PetBreed breed = petBreedRepository.findById(breedId);
        if (breed == null) {
            throw new InvalidOperationException(CommonMessageKeys.NO_OPERATION_ENTITY);
        }

        breed.setName(name);
        return petBreedRepository.save(breed);
    }

}
