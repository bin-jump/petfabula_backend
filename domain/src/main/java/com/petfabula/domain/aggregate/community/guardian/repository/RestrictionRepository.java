package com.petfabula.domain.aggregate.community.guardian.repository;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;

public interface RestrictionRepository {

    Restriction save(Restriction restriction);

    Restriction findByParticipatorId(Long participatorId);

    void remove(Restriction restriction);
}
