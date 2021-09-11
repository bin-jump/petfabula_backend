package com.petfabula.infrastructure.persistence.jpa.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.ReportReason;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReportReasonJpaRepository extends JpaRepository<ReportReason, Long> {

    ReportReason findByReportIdAndReporterId(Long reportId, Long reporterId);

    Page<ReportReason> findAllByReportId(Long reportId, Pageable pageable);
}
