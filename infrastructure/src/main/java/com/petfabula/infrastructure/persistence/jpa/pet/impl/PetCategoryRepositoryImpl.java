package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import com.petfabula.domain.aggregate.pet.respository.PetCategoryRepository;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.PetCategoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetCategoryRepositoryImpl implements PetCategoryRepository {

    @Autowired
    private PetCategoryJpaRepository petCategoryJpaRepository;

    @Override
    public PetCategory save(PetCategory petCategory) {
        return petCategoryJpaRepository.save(petCategory);
    }

    @Override
    public PetCategory findById(Long id) {
        return petCategoryJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PetCategory findByName(String name) {
        return petCategoryJpaRepository.findByName(name);
    }

    @Override
    public List<PetCategory> findAll() {
        return petCategoryJpaRepository.findAll();
    }
}
