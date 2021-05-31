package com.petfabula.infrastructure.persistence.jpa.idgenerator.repository;

import com.petfabula.domain.common.idgenerator.IdSegment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdSegmentJpaRepository extends JpaRepository<IdSegment, Integer> {

    IdSegment findByServiceTag(String serviceTag);

    @Modifying
    @Query("update IdSegment seg set seg.maxId = seg.maxId + seg.step where seg.serviceTag = :serviceTag")
    void updateSegmentMaxIdByStep(@Param("serviceTag")String serviceTag);
}
