package com.petfabula.infrastructure.persistence.jpa.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipatorPetJpaRepository extends JpaRepository<ParticipatorPet, Long> {

    @Query("select p from ParticipatorPet p where p.id = :id")
    Optional<ParticipatorPet> findById(Long id);

    List<ParticipatorPet> findByParticipatorId(Long participatorId);
}
