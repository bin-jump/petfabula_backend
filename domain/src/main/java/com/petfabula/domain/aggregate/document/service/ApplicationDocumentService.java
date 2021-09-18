package com.petfabula.domain.aggregate.document.service;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.domain.aggregate.document.repository.ApplicationDocumentRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationDocumentService {

    public static String USER_AGREEMENT_KEY = "USER_AGREEMENT";

    public static String PRIVACY_AGREEMENT_KEY = "PRIVACY_AGREEMENT";


    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    @Autowired
    private DocumentIdGenerator idGenerator;

    public ApplicationDocument create(String key, String content) {
        ApplicationDocument document = applicationDocumentRepository.findByDocumentKey(key);
        if (document != null) {
            return document;
        }

        Long id = idGenerator.nextId();
        document = new ApplicationDocument(id, key, content);
        return applicationDocumentRepository.save(document);
    }

    public ApplicationDocument update(String key, String content) {
        ApplicationDocument document = applicationDocumentRepository.findByDocumentKey(key);
        if (document == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        document.setContent(content);
        return applicationDocumentRepository.save(document);
    }
}
