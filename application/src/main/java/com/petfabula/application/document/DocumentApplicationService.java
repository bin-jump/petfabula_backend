package com.petfabula.application.document;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.domain.aggregate.document.repository.ApplicationDocumentRepository;
import com.petfabula.domain.aggregate.document.service.ApplicationDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DocumentApplicationService {

    @Autowired
    private ApplicationDocumentService applicationDocumentService;

    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    @Transactional
    public ApplicationDocument updateDocument(String key, String content) {
        return applicationDocumentService.update(key, content);
    }

    public ApplicationDocument getUserAgreementDocument() {

        return applicationDocumentRepository
                .findByDocumentKey(ApplicationDocumentService.USER_AGREEMENT_KEY);
    }

    public ApplicationDocument getPrivacyAgreementDocument() {

        return applicationDocumentRepository
                .findByDocumentKey(ApplicationDocumentService.PRIVACY_AGREEMENT_KEY);
    }
}
