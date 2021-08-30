package com.petfabula.infrastructure.persistence.jpa.pet.repository;

import com.petfabula.domain.aggregate.pet.entity.GenericPetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface GenericPetRecordJpaRepository extends JpaRepository<GenericPetRecord, Long>, JpaSpecificationExecutor {

    @Query("select r from GenericPetRecord r where r.id = :id")
    Optional<GenericPetRecord> findById(Long id);

}
