package com.petfabula.presentation.facade.dto.community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipatorPetDto {

    private Long id;

    private String name;

    private String photo;

    private String category;

    private Long breedId;
}
