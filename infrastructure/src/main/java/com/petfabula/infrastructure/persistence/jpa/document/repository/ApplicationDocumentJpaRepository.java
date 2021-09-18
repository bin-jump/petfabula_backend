package com.petfabula.infrastructure.persistence.jpa.document.repository;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationDocumentJpaRepository extends JpaRepository<ApplicationDocument, Long> {

    ApplicationDocument findByDocumentKey(String documentKey);

}
