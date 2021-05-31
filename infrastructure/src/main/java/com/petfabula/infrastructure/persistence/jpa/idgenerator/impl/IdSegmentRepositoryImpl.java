package com.petfabula.infrastructure.persistence.jpa.idgenerator.impl;

import com.petfabula.domain.common.idgenerator.IdSegment;
import com.petfabula.domain.common.idgenerator.IdSegmentRepository;
import com.petfabula.infrastructure.persistence.jpa.idgenerator.repository.IdSegmentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class IdSegmentRepositoryImpl implements IdSegmentRepository {

    @Autowired
    private IdSegmentJpaRepository idSegmentJpaRepository;

    @Override
    public IdSegment save(IdSegment idSegment) {
        return idSegmentJpaRepository.save(idSegment);
    }

    @Override
    public IdSegment findByServiceTag(String serviceTag) {
        return idSegmentJpaRepository.findByServiceTag(serviceTag);
    }

    @Override
    public void updateSegmentMaxIdByStep(String serviceTag) {
        idSegmentJpaRepository.updateSegmentMaxIdByStep(serviceTag);
    }
}
