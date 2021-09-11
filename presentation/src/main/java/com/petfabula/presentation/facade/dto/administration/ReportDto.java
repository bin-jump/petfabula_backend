package com.petfabula.presentation.facade.dto.administration;

import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDto {

    private Long id;

    private Long entityId;

    private String entityType;

    private Report.ReportStatus status;

    private Integer reportCount;

    private Long recentReporterId;

    private String recentReason;

    private ParticipatorDto recentReporter;

    private Object entity;
}
