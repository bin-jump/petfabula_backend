package com.petfabula.presentation.facade.dto.administration;

import com.petfabula.domain.aggregate.administration.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateReportStatusRequest {

    @NotNull
    private Long id;

    @NotNull
    private Report.ReportStatus status;
}
