package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorPetRepository;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.repository.ParticipatorPetJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParticipatorPetRepositoryImpl implements ParticipatorPetRepository {

    @Autowired
    private ParticipatorPetJpaRepository participatorPetJpaRepository;

    @Override
    public ParticipatorPet save(ParticipatorPet participatorPet) {
        return participatorPetJpaRepository.save(participatorPet);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public ParticipatorPet findById(Long id) {
        return participatorPetJpaRepository.findById(id).orElse(null);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public List<ParticipatorPet> findByParticipatorId(Long participatorId) {
        return participatorPetJpaRepository.findByParticipatorId(participatorId);
    }
}
