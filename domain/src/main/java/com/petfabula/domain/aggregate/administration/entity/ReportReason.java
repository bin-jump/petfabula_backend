package com.petfabula.domain.aggregate.administration.entity;

import com.petfabula.domain.common.domain.EntityBase;
import com.petfabula.domain.common.domain.GeneralEntity;
import com.petfabula.domain.common.util.ValueUtil;
import com.petfabula.domain.common.validation.EntityValidationUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_report_reason", uniqueConstraints={
        @UniqueConstraint(columnNames = {"report_id", "reporter_id"})})
public class ReportReason extends GeneralEntity {

    public ReportReason(Long id, Long reportId, Long reporterId, String reason) {
        setId(id);
        this.reportId = reportId;
        this.reporterId = reporterId;
        setReason(reason);
    }

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Column(name = "reason", nullable = false, length = 1000)
    private String reason;

    public void setReason(String reason) {
        EntityValidationUtils.validStringLength("reason", reason, 10, 500);
        reason = ValueUtil.trimContent(reason);
        this.reason = reason;
    }
}
