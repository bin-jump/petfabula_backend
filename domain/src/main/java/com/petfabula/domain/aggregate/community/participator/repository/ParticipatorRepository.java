package com.petfabula.domain.aggregate.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;

import java.util.List;

public interface ParticipatorRepository {

    Participator save(Participator participator);

    Participator findById(Long id);

    List<Participator> findByIds(List<Long> ids);
}
