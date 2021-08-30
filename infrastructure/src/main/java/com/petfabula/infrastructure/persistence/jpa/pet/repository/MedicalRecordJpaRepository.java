package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordJpaRepository extends JpaRepository<MedicalRecord, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "medicalRecord.all")
    @Query("select r from MedicalRecord r where r.id = :id")
    Optional<MedicalRecord> findById(Long id);

    @EntityGraph(value = "medicalRecord.all")
    List<MedicalRecord> findByIdInOrderByIdDesc(List<Long> ids);
}
