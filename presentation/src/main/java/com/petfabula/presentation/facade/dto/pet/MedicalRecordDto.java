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
public class MedicalRecordDto {

    private Long id;

    private Long petId;

    private String hospitalName;

    private String symptom;

    private String diagnosis;

    private String treatment;

    private Long date;

    private String note;

    @Builder.Default
    private List<ImageDto> images = new ArrayList<>();
}
