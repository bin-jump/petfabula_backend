package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.PetEventType;
import com.petfabula.domain.aggregate.pet.respository.PetEventTypeRepository;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.PetEventTypeJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PetEventTypeRepositoryImpl implements PetEventTypeRepository {

    @Autowired
    private PetEventTypeJpaRepository petEventTypeJpaRepository;

    @Override
    public PetEventType save(PetEventType petEventType) {
        return petEventTypeJpaRepository.save(petEventType);
    }

    @Override
    public List<PetEventType> findAll() {
        return petEventTypeJpaRepository.findAll();
    }

    @Override
    public PetEventType findByEventType(String eventType) {
        return petEventTypeJpaRepository.findByEventType(eventType);
    }
}
