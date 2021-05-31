package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.entity.Participator;
import com.petfabula.domain.aggregate.community.repository.ParticipatorRepository;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.repository.ParticipatorJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class ParticipatorRepositoryImpl implements ParticipatorRepository {

    @Autowired
    private ParticipatorJpaRepository participatorJpaRepository;

    @Override
    public Participator save(Participator participator) {
        return participatorJpaRepository.save(participator);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public Participator findById(Long id) {
        return participatorJpaRepository.findById(id).orElse(null);
    }
}
