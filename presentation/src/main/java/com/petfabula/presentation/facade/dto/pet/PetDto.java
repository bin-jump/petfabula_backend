package com.petfabula.presentation.facade.dto.pet;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.PetCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PetDto {

    private Long id;

    private Long feederId;

    @NotNull
    private String name;

    private String photo;

    @NotNull
    private Long birthday;

    @NotNull
    private Pet.Gender gender;

    @NotNull
    private String category;

    @NotNull
    private String breed;

    private String bio;
}
