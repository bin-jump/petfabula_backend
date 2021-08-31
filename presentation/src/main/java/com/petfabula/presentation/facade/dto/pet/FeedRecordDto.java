package com.petfabula.presentation.facade.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedRecordDto {

    private Long id;

    private Long petId;

    @NotNull
    private Long dateTime;

    private String foodContent;

    @NotNull
    private Integer amount;

    private String note;

    private PetDto pet;
}
