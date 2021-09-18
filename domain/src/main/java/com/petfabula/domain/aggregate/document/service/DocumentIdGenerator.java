package com.petfabula.domain.aggregate.document.service;

import com.petfabula.domain.common.domain.idgenerator.EntityIdGenerator;
import com.petfabula.domain.common.domain.idgenerator.IdSegment;
import org.springframework.stereotype.Component;

@Component
public class DocumentIdGenerator extends EntityIdGenerator {

    public static final int STEP = 3000;

    public static final String SERVICE_TAG = "document_service";

    public static final Long START_ID = 1L;

    @Override
    public IdSegment createInitialSegment() {
        return new IdSegment(SERVICE_TAG, STEP, START_ID);
    }

    @Override
    public String getServiceTag() {
        return SERVICE_TAG;
    }
}
