package com.petfabula.presentation.facade.dto.identity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityDto {

    private Long id;

    private String name;

    private String prefectureName;

    private Long prefectureId;
}
