package com.petfabula.infrastructure.persistence.jpa.community.participator.repository;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ParticipatorJpaRepository extends JpaRepository<Participator, Long>, JpaSpecificationExecutor {

    List<Participator> findByIdInOrderByIdDesc(List<Long> ids);

    @Query("select u from Participator u where u.id = :id")
    Optional<Participator> findById(Long id);
}
