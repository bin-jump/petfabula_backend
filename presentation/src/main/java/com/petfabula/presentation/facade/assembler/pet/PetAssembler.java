package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.Pet;
import com.petfabula.domain.aggregate.pet.entity.PetBreed;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.pet.PetBreedDto;
import com.petfabula.presentation.facade.dto.pet.PetDetailDto;
import com.petfabula.presentation.facade.dto.pet.PetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PetAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;


    public PetDto convertToDto(Pet pet) {
        PetDto petDto = modelMapper.map(pet, PetDto.class);
        if (petDto.getPhoto() != null) {
            petDto.setPhoto(assemblerHelper.completeImageUrl(petDto.getPhoto()));
        }
        return petDto;
    }

    public PetDetailDto convertToDetailDto(Pet pet) {
        PetDetailDto petDto = modelMapper.map(pet, PetDetailDto.class);
        if (petDto.getPhoto() != null) {
            petDto.setPhoto(assemblerHelper.completeImageUrl(petDto.getPhoto()));
        }
        return petDto;
    }

    public List<PetDto> convertToDtos(List<Pet> pets) {
        return pets.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public List<PetDetailDto> convertToDetailDtos(List<Pet> pets) {
        return pets.stream().map(this::convertToDetailDto).collect(Collectors.toList());
    }

    public PetBreedDto convertToPetBreedDto(PetBreed petBreed) {
        PetBreedDto breedDto = PetBreedDto.builder()
                .id(petBreed.getId())
                .category(petBreed.getCategory())
                .categoryId(petBreed.getCategoryId())
                .name(petBreed.getName())
                .build();
        return breedDto;
    }

    public List<PetBreedDto> convertToPetBreedDtos(List<PetBreed> petBreeds) {
        return petBreeds.stream().map(this::convertToPetBreedDto).collect(Collectors.toList());

    }
}
