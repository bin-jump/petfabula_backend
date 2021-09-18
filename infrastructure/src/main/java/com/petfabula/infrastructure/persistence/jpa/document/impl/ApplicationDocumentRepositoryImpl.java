package com.petfabula.infrastructure.persistence.jpa.document.impl;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.domain.aggregate.document.repository.ApplicationDocumentRepository;
import com.petfabula.infrastructure.persistence.jpa.document.repository.ApplicationDocumentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDocumentRepositoryImpl implements ApplicationDocumentRepository {

    @Autowired
    private ApplicationDocumentJpaRepository applicationDocumentJpaRepository;

    @Override
    public ApplicationDocument save(ApplicationDocument document) {
        return applicationDocumentJpaRepository.save(document);
    }

    @Override
    public ApplicationDocument findById(Long id) {
        return applicationDocumentJpaRepository.findById(id).orElse(null);
    }

    @Override
    public ApplicationDocument findByDocumentKey(String key) {
        return applicationDocumentJpaRepository.findByDocumentKey(key);
    }

    @Override
    public void remove(ApplicationDocument document) {
        applicationDocumentJpaRepository.delete(document);
    }
}
