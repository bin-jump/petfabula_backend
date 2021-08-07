package com.petfabula.presentation.facade.dto.pet;

import com.petfabula.presentation.facade.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetEventRecordDto {

    private Long id;

    private Long petId;

    private Long date;

    private String eventType;

    private String note;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}
