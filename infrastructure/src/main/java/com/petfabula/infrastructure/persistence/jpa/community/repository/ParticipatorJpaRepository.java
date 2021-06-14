package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParticipatorJpaRepository extends JpaRepository<Participator, Long>, JpaSpecificationExecutor {
}
