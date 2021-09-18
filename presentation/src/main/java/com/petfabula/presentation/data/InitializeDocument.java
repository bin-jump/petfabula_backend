package com.petfabula.presentation.data;

import com.petfabula.domain.aggregate.document.entity.ApplicationDocument;
import com.petfabula.domain.aggregate.document.repository.ApplicationDocumentRepository;
import com.petfabula.domain.aggregate.document.service.ApplicationDocumentService;
import com.petfabula.domain.aggregate.document.service.DocumentIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class InitializeDocument implements ApplicationRunner {

    @Autowired
    private ApplicationDocumentService documentService;

    @Autowired
    private ApplicationDocumentRepository applicationDocumentRepository;

    @Value("classpath:data/initial_user_agreement.txt")
    private Resource userAgreementResource;

    @Value("classpath:data/initial_privacy_agreement.txt")
    private Resource privacyAgreementResource;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        createFromResource(userAgreementResource, ApplicationDocumentService.USER_AGREEMENT_KEY);
        createFromResource(privacyAgreementResource, ApplicationDocumentService.PRIVACY_AGREEMENT_KEY);
    }

    private void createFromResource(Resource resource, String key) throws IOException {
        ApplicationDocument userAgreementDocument =
                applicationDocumentRepository.findByDocumentKey(key);

        if (userAgreementDocument == null) {
            InputStream is = resource.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String content = reader.lines().collect(Collectors.joining("\n"));

            documentService.create(key, content);
        }
    }
}
