package com.petfabula.presentation.facade.dto.administration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestrictionDto {

    private Long id;

    @NotNull
    private Long participatorId;

    private Long expiration;

    private String reason;
}
