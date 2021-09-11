package com.petfabula.domain.aggregate.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.ReportReason;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

public interface ReportReasonRepository {

    ReportReason save(ReportReason reportReason);

    ReportReason findByReportIdAndReporterId(Long reportId, Long reporterId);

    JumpableOffsetPage<ReportReason> findByReportId(Long reportId, int pageIndex, int pageSize);
}
