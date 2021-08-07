package com.petfabula.presentation.facade.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedRecordDto {

    private Long id;

    private Long petId;

    private Long date;

    private String foodContent;

    private Integer amount;

    private String note;
}
