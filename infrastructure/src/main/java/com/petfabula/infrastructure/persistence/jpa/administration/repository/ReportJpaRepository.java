package com.petfabula.infrastructure.persistence.jpa.administration.repository;

import com.petfabula.domain.aggregate.administration.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportJpaRepository extends JpaRepository<Report, Long> {

    Report findByEntityIdAndEntityType(Long entityId, String entityType);
}
