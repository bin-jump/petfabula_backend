package com.petfabula.presentation.facade.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateReportRequest {

    @NotNull
    private Long entityId;

    @NotEmpty
    private String entityType;

    @NotEmpty
    private String reason;
}
