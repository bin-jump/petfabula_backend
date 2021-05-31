package com.petfabula.domain.aggregate.community.repository;

import com.petfabula.domain.aggregate.community.entity.Participator;

public interface ParticipatorRepository {

    Participator save(Participator participator);

    Participator findById(Long id);
}
