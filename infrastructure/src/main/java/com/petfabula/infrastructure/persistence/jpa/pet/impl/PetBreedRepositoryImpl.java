package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.domain.aggregate.pet.respository.PetBreedRepository;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.PetBreedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PetBreedRepositoryImpl implements PetBreedRepository {

    @Autowired
    private PetBreedJpaRepository petBreedJpaRepository;

    @Override
    public PetBreed save(PetBreed petBreed) {
        return petBreedJpaRepository.save(petBreed);
    }

    @Override
    public List<PetBreed> findAll() {
        return petBreedJpaRepository.findAll();
    }

    @Override
    public List<PetBreed> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return petBreedJpaRepository.findByIdIn(ids);
    }

    @Override
    public PetBreed findById(Long id) {
        return petBreedJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PetBreed findByCategoryIdName(Long categoryId, String name) {
        return petBreedJpaRepository.findByCategoryIdAndName(categoryId, name);
    }
}
