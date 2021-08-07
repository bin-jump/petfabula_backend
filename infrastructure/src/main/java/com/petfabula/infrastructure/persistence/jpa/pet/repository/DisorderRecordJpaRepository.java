package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DisorderRecordJpaRepository extends JpaRepository<DisorderRecord, Long>, JpaSpecificationExecutor {

    DisorderRecord findByPetId(Long petId);
}
