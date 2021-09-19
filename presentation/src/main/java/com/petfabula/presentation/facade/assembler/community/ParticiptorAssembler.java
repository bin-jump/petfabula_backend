package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ParticiptorAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public ParticipatorDto convertToDto(Participator participator) {
        ParticipatorDto participatorDto = modelMapper.map(participator, ParticipatorDto.class);
        if (participatorDto.getPhoto() != null) {
            participatorDto.setPhoto(assemblerHelper.completeImageUrl(participatorDto.getPhoto()));
        }
        return participatorDto;
    }

    public List<ParticipatorDto> convertToDtos(List<Participator> participators) {
        return participators.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
