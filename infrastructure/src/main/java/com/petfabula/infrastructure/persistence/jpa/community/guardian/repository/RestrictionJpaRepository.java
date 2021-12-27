package com.petfabula.infrastructure.persistence.jpa.community.guardian.repository;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestrictionJpaRepository extends JpaRepository<Restriction, Long> {

    Restriction findByParticipatorId(Long participatorId);
}
