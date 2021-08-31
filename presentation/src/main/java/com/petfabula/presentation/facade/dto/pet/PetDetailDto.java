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
public class PetDetailDto {

    private Long id;

    private Long feederId;

    @NotNull
    private String name;

    private String photo;

    @NotNull
    private Long birthday;

    @NotNull
    private Long arrivalDay;

    @NotNull
    private Pet.Gender gender;

    private Integer weight;

    private Long categoryId;

    private String category;

    @NotNull
    private Long breedId;

    private PetBreedDto breed;

    private String bio;

    private Integer feedRecordCount;

    private Integer disorderRecordCount;

    private Integer medicalRecordCount;

    private Integer weightRecordCount;

    private Integer eventRecordCount;
}
