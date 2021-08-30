package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FeedRecordJpaRepository extends JpaRepository<FeedRecord, Long>, JpaSpecificationExecutor {

    @Query("select r from FeedRecord r where r.id = :id")
    Optional<FeedRecord> findById(Long id);

    FeedRecord findByPetId(Long petId);
}
