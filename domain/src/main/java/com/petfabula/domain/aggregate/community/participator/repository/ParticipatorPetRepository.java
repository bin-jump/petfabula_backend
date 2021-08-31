package com.petfabula.domain.aggregate.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;

import java.util.List;

public interface ParticipatorPetRepository {

    ParticipatorPet save(ParticipatorPet participatorPet);

    ParticipatorPet findById(Long id);

    List<ParticipatorPet> findByParticipatorId(Long participatorId);

    void remove(ParticipatorPet participatorPet);
}
