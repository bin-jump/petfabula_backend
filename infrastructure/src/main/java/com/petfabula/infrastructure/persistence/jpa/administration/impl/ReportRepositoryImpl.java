package com.petfabula.infrastructure.persistence.jpa.administration.impl;

import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.repository.ReportRepository;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.infrastructure.persistence.jpa.administration.repository.ReportJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class ReportRepositoryImpl implements ReportRepository {

    @Autowired
    private ReportJpaRepository reportJpaRepository;

    @Override
    public Report save(Report report) {
        return reportJpaRepository.save(report);
    }

    @Override
    public Report findById(Long id) {
        return reportJpaRepository.findById(id).orElse(null);
    }

    @Override
    public JumpableOffsetPage<Report> find(int pageIndex, int pageSize) {
        Pageable sortedById = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<Report> reasonPage = reportJpaRepository.findAll(sortedById);

        int cnt = (int) reasonPage.getTotalElements();
        return JumpableOffsetPage.of(reasonPage.getContent(), pageIndex, pageSize, cnt);
    }

    @Override
    public Report findByEntityIdAndEntityType(Long entityId, String entityType) {
        return reportJpaRepository.findByEntityIdAndEntityType(entityId, entityType);
    }
}
