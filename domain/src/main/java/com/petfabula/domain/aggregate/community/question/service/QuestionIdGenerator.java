package com.petfabula.domain.aggregate.community.question.service;

import com.petfabula.domain.common.domain.idgenerator.EntityIdGenerator;
import com.petfabula.domain.common.domain.idgenerator.IdSegment;
import org.springframework.stereotype.Component;

@Component
public class QuestionIdGenerator extends EntityIdGenerator {

    public static final int STEP = 6000;

    public static final String SERVICE_TAG = "question_service";

    public static final Long START_ID = 1L;

    @Override
    public String getServiceTag() {
        return SERVICE_TAG;
    }

    @Override
    public IdSegment createInitialSegment() {
        return new IdSegment(SERVICE_TAG, STEP, START_ID);
    }
}
