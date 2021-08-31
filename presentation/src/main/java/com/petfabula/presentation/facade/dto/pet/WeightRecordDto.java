package com.petfabula.presentation.facade.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeightRecordDto {

    private Long id;

    private Long petId;

    private Long dateTime;

    private Integer weight;

    private PetDto pet;
}
