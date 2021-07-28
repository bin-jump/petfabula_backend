package com.petfabula.infrastructure.persistence.jpa.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ParticipatorJpaRepository extends JpaRepository<Participator, Long>, JpaSpecificationExecutor {

    List<Participator> findByIdIn(List<Long> ids);
}
