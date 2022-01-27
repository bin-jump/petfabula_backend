package com.petfabula.infrastructure.persistence.jpa.identity.repository;

import com.petfabula.domain.aggregate.identity.entity.EmailCodeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeRecordJpaRepository extends JpaRepository<EmailCodeRecord, Long> {
    EmailCodeRecord findByEmail(String email);
}
