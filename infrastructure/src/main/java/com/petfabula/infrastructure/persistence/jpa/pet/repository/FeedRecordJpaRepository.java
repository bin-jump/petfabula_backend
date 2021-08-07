package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeedRecordJpaRepository extends JpaRepository<FeedRecord, Long>, JpaSpecificationExecutor {

    FeedRecord findByPetId(Long petId);
}
