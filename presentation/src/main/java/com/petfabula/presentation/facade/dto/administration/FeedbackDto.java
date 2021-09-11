package com.petfabula.presentation.facade.dto.administration;

import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
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
public class FeedbackDto {

    private Long id;

    @NotEmpty
    private String content;

    private Long reporterId;

    private ParticipatorDto reporter;
}
