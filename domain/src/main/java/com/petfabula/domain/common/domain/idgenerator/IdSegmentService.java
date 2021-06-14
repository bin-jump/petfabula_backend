package com.petfabula.domain.common.domain.idgenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class IdSegmentService {

    @Autowired
    private IdSegmentRepository idSegmentRepository;

    @Transactional
    public IdSegment aquireAndUpdateSegment(String serviceTag) {
        idSegmentRepository.updateSegmentMaxIdByStep(serviceTag);
        return idSegmentRepository.findByServiceTag(serviceTag);
    }
}
