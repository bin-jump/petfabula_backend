package com.petfabula.domain.aggregate.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;

public interface ParticipatorRepository {

    Participator save(Participator participator);

    Participator findById(Long id);
}
