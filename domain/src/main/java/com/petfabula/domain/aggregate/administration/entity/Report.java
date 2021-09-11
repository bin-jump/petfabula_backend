package com.petfabula.domain.aggregate.administration.entity;

import com.petfabula.domain.common.domain.ConcurrentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "user_report", uniqueConstraints={
        @UniqueConstraint(columnNames = {"entity_id", "entity_type"})})
public class Report extends ConcurrentEntity {

    public Report(Long id, String entityType, Long entityId,
                  Long recentReporterId, String recentReason) {
        setId(id);
        this.entityId = entityId;
        this.entityType = entityType;
        this.status = ReportStatus.CREATED;
        this.reportCount = 1;
        this.recentReporterId = recentReporterId;
        this.recentReason = recentReason;
        this.memo = "";
    }

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entity_type", length = 16)
    private String entityType;

    @Column(name = "status")
    private ReportStatus status;

    @Column(name = "report_count", nullable = false)
    private Integer reportCount;

    @Column(name = "recent_reporter_id", nullable = false)
    private Long recentReporterId;

    @Column(name = "recent_reason", nullable = false)
    private String recentReason;

    @Column(name = "memo")
    private String memo;

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public void setRecentReporterId(Long recentReporterId) {
        this.recentReporterId = recentReporterId;
    }

    public void setRecentReason(String recentReason) {
        this.recentReason = recentReason;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public enum ReportStatus {
        CREATED, DONE,  RETAIN
    }

    public enum ReportType {
        POST, QUESTION, ANSWER, POST_COMMENT, POST_COMMENT_REPLY, ANSWER_COMMENT
    }
}
