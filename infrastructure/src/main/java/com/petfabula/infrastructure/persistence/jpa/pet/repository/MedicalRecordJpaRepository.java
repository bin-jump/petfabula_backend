package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecord, Long>, JpaSpecificationExecutor {
}
