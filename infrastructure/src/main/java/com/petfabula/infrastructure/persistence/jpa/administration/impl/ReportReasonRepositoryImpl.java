package com.petfabula.infrastructure.persistence.jpa.administration.impl;

import com.petfabula.domain.aggregate.administration.entity.ReportReason;
import com.petfabula.domain.aggregate.administration.repository.ReportReasonRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.infrastructure.persistence.jpa.administration.repository.ReportReasonJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ReportReasonRepositoryImpl implements ReportReasonRepository {

    @Autowired
    private ReportReasonJpaRepository reportReasonJpaRepository;

    @Override
    public ReportReason save(ReportReason reportReason) {
        return reportReasonJpaRepository.save(reportReason);
    }

    @Override
    public ReportReason findByReportIdAndReporterId(Long reportId, Long reporterId) {
        return reportReasonJpaRepository.findByReportIdAndReporterId(reportId, reporterId);
    }

    @Override
    public JumpableOffsetPage<ReportReason> findByReportId(Long reportId, int pageIndex, int pageSize) {
        Pageable sortedById = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<ReportReason> reasonPage = reportReasonJpaRepository.findAllByReportId(reportId, sortedById);

        int cnt = (int) reasonPage.getTotalElements();

        return JumpableOffsetPage.of(reasonPage.getContent(), pageIndex, pageSize, cnt);
    }
}
