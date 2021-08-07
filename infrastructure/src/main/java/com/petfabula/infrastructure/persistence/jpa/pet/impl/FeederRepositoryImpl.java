package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.Feeder;
import com.petfabula.domain.aggregate.pet.respository.FeederRepository;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.FeederJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FeederRepositoryImpl implements FeederRepository {

    @Autowired
    private FeederJpaRepository feederJpaRepository;

    @Override
    public Feeder save(Feeder feeder) {
        return feederJpaRepository.save(feeder);
    }

    @Override
    public Feeder findById(Long id) {
        return feederJpaRepository.findById(id).orElse(null);
    }
}
