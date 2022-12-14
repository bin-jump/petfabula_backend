package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.participator.entity.ParticipatorPet;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.community.ParticipatorPetDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticiptorPetAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public ParticipatorPetDto convertToDto(ParticipatorPet participatorPet) {
        ParticipatorPetDto participatorPetDto = modelMapper.map(participatorPet, ParticipatorPetDto.class);
        if (participatorPetDto.getPhoto() != null) {
            participatorPetDto.setPhoto(assemblerHelper.completeImageUrl(participatorPetDto.getPhoto()));
        }
        return participatorPetDto;
    }

    public List<ParticipatorPetDto> convertToDtos(List<ParticipatorPet> participatorPets) {
        return participatorPets.stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }
}
