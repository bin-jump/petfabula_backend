package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.respository.PetRepository;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.PetJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PetRepositoryImpl implements PetRepository {

    @Autowired
    private PetJpaRepository petJpaRepository;

    @Override
    public Pet save(Pet pet) {
        return petJpaRepository.save(pet);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public Pet findById(Long id) {
        return petJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public Pet findByName(String name) {
        return petJpaRepository.findByName(name);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public List<Pet> findByFeederId(Long id) {
        return petJpaRepository.findByFeederId(id);
    }
}
