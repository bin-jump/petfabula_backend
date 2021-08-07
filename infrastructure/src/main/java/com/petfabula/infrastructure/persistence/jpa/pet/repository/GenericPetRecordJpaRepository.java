package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.GenericPetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GenericPetRecordJpaRepository extends JpaRepository<GenericPetRecord, Long>, JpaSpecificationExecutor {
}
