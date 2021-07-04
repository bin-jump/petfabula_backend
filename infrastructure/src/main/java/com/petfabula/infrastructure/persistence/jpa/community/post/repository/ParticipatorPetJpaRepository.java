package com.petfabula.infrastructure.persistence.jpa.community.post.repository;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipatorPetJpaRepository extends JpaRepository<ParticipatorPet, Long> {

    List<ParticipatorPet> findByParticipatorId(Long participatorId);
}
