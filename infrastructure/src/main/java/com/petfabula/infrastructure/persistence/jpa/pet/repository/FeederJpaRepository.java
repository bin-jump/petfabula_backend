package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.Feeder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FeederJpaRepository extends JpaRepository<Feeder, Long> {

    @Query("select f from Feeder f where f.id = :id")
    Optional<Feeder> findById(Long id);
}
