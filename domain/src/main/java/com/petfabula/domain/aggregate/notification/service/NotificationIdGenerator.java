package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.common.domain.idgenerator.EntityIdGenerator;
import com.petfabula.domain.common.domain.idgenerator.IdSegment;
import org.springframework.stereotype.Component;

@Component
public class NotificationIdGenerator  extends EntityIdGenerator {

    public static final int STEP = 10000;

    public static final String SERVICE_TAG = "notification_service";

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