package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParticiptorAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public ParticipatorDto convertToDto(Participator participator) {
        ParticipatorDto participatorDto = modelMapper.map(participator, ParticipatorDto.class);
        return participatorDto;
    }
}
