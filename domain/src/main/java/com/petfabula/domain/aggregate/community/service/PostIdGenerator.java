package com.petfabula.domain.aggregate.community.service;

import com.petfabula.domain.common.idgenerator.EntityIdGenerator;
import com.petfabula.domain.common.idgenerator.IdSegment;
import org.springframework.stereotype.Component;

@Component
public class PostIdGenerator extends EntityIdGenerator {

    public static final int STEP = 6000;

    public static final String SERVICE_TAG = "post_service";

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
