package com.petfabula.domain.common.idgenerator;

public interface IdSegmentRepository {

    IdSegment save(IdSegment idSegment);

    IdSegment findByServiceTag(String serviceTag);

    void updateSegmentMaxIdByStep(String serviceTag);

}
