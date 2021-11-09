package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PetEventRecordJpaRepository extends JpaRepository<PetEventRecord, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "petEventRecord.all")
    @Query("select r from PetEventRecord r where r.id = :id")
    Optional<PetEventRecord> findById(Long id);

    @EntityGraph(value = "petEventRecord.all")
    List<PetEventRecord> findByIdInOrderByIdDesc(List<Long> ids);

    PetEventRecord findByPetIdAndDateTime(Long petId, Instant dateTime);
}
