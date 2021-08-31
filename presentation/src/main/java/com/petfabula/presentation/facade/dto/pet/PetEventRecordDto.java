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

    private Long dateTime;

    private String eventType;

    private String content;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();

    private PetDto pet;
}
