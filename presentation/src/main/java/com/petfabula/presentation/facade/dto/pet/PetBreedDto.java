package com.petfabula.presentation.facade.dto.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetBreedDto {

    private Long id;

    private Long categoryId;

    private String category;

    private String name;
}