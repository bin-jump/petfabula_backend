package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PetEventRecordJpaRepository extends JpaRepository<PetEventRecord, Long>, JpaSpecificationExecutor {
}
