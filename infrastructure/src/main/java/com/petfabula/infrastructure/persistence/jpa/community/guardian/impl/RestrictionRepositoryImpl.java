package com.petfabula.infrastructure.persistence.jpa.community.guardian.impl;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.domain.aggregate.community.guardian.repository.RestrictionRepository;
import com.petfabula.infrastructure.persistence.jpa.community.guardian.repository.RestrictionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RestrictionRepositoryImpl implements RestrictionRepository {

    @Autowired
    private RestrictionJpaRepository restrictionJpaRepository;

    @Override
    public Restriction save(Restriction restriction) {
        return restrictionJpaRepository.save(restriction);
    }

    @Override
    public Restriction findByParticipatorId(Long participatorId) {
        return restrictionJpaRepository.findByParticipatorId(participatorId);
    }

    @Override
    public void remove(Restriction restriction) {
        restrictionJpaRepository.delete(restriction);
    }
}
