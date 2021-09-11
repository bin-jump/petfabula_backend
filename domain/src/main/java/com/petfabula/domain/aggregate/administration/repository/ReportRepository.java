package com.petfabula.domain.aggregate.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

public interface ReportRepository {

    Report save(Report report);

    Report findById(Long id);

    JumpableOffsetPage<Report> find(int pageIndex, int pageSize);

    Report findByEntityIdAndEntityType(Long entityId, String entityType);
}
