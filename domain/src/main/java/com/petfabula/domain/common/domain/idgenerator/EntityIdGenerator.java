package com.petfabula.domain.common.domain.idgenerator;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

/***
 * This class must be singleton
 */
public abstract class EntityIdGenerator {

    private final int MAX_ID_STEP = 10;

    private IdSegmentService idSegmentService;

    private Random random = new Random();

    private long currentMax;

    private long currentId;

    private boolean initialized;

    @Autowired
    public void setIdSegmentService(IdSegmentService idSegmentService) {
        this.idSegmentService = idSegmentService;
    }


    public void refreshSegment() {
        IdSegment segment = idSegmentService.aquireAndUpdateSegment(getServiceTag());
        currentMax = segment.getMaxId() + segment.getStep();
        currentId = segment.getMaxId() + 1;
    }

    public synchronized Long nextId() {
        // skip first segment for simplicity
        if (!initialized) {
            refreshSegment();
            initialized = true;
            return currentId;
        }

//        int step = 1;
        // make id sparse against robot
        int step = random.nextInt(MAX_ID_STEP - 1) + 1;
        currentId = Math.min(currentMax, currentId + step);
        if (currentId == currentMax) {
            refreshSegment();
        }
        return currentId;
    }

    public abstract String getServiceTag();

    public abstract IdSegment createInitialSegment();
}
