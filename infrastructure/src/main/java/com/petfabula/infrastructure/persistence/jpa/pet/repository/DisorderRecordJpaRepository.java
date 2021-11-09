package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DisorderRecordJpaRepository extends JpaRepository<DisorderRecord, Long>, JpaSpecificationExecutor {

    @EntityGraph(value = "disorderRecord.all")
    @Query("select r from DisorderRecord r where r.id = :id")
    Optional<DisorderRecord> findById(Long id);

    @EntityGraph(value = "disorderRecord.all")
    List<DisorderRecord> findByIdInOrderByIdDesc(List<Long> ids);

    DisorderRecord findByPetId(Long petId);

    DisorderRecord findByPetIdAndDateTime(Long petId, Instant dateTime);
}
