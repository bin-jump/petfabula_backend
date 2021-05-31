package com.petfabula.infrastructure.persistence.jpa.community.repository;

import com.petfabula.domain.aggregate.community.entity.ParticipatorPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipatorPetJpaRepository extends JpaRepository<ParticipatorPet, Long> {

    List<ParticipatorPet> findByParticipatorId(Long participatorId);
}
