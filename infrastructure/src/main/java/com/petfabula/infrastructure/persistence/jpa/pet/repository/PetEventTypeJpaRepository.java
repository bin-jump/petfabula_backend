package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.PetEventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetEventTypeJpaRepository extends JpaRepository<PetEventType, Long> {

    PetEventType findByEventType(String eventType);
}
