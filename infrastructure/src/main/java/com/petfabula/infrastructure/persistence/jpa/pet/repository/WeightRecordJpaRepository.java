package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface WeightRecordJpaRepository extends JpaRepository<WeightRecord, Long>, JpaSpecificationExecutor {

    @Query("select r from WeightRecord r where r.id = :id")
    Optional<WeightRecord> findById(Long id);

    WeightRecord findByPetIdAndDateTime(Long petId, Instant dateTime);
}
