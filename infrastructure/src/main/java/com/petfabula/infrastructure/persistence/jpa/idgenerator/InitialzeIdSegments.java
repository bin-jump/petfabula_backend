package com.petfabula.infrastructure.persistence.jpa.idgenerator;

import com.petfabula.domain.common.domain.idgenerator.EntityIdGenerator;
import com.petfabula.domain.common.domain.idgenerator.IdSegment;
import com.petfabula.infrastructure.persistence.jpa.idgenerator.impl.IdSegmentRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@Order(1)
public class InitialzeIdSegments implements ApplicationRunner {

    @Autowired
    private IdSegmentRepositoryImpl idSegmentRepository;

    @Autowired
    private List<EntityIdGenerator> entityIdGenerators;

    @Override
    public void run(ApplicationArguments args) {

        List<IdSegment> segments = getServiceIdSegments();
        for(IdSegment segment : segments) {
            insertSegment(segment);
        }

    }

    private void insertSegment(IdSegment segment) {
        try {
            if (idSegmentRepository.findByServiceTag(segment.getServiceTag()) == null) {
                idSegmentRepository.save(segment);
            }
        } catch(DataIntegrityViolationException ex) {
            // just skip if segment is inserted by other application instances concurrently
        }

    }

    private List<IdSegment> getServiceIdSegments() {
        List<IdSegment> res = new ArrayList<>();
        for(EntityIdGenerator generator : entityIdGenerators) {
            res.add(generator.createInitialSegment());
        }
        return res;
    }
}
