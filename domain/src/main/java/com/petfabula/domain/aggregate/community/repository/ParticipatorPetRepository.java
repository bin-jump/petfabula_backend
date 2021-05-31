package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.ParticipatorPet;

import java.util.List;

public interface ParticipatorPetRepository {

    ParticipatorPet save(ParticipatorPet participatorPet);

    ParticipatorPet findById(Long id);

    List<ParticipatorPet> findByParticipatorId(Long participatorId);
}
