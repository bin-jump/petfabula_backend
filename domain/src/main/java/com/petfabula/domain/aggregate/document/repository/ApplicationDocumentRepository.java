package com.petfabula.domain.aggregate.document.repository;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;

public interface ApplicationDocumentRepository {

    ApplicationDocument save(ApplicationDocument document);

    ApplicationDocument findById(Long id);

    ApplicationDocument findByDocumentKey(String key);

    void remove(ApplicationDocument document);
}
