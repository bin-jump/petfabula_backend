package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.Feeder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeederJpaRepository extends JpaRepository<Feeder, Long> {
}
