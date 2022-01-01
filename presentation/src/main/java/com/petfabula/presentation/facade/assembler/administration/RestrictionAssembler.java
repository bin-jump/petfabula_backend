package com.petfabula.presentation.facade.assembler.administration;

import com.petfabula.domain.aggregate.community.guardian.entity.Restriction;
import com.petfabula.presentation.facade.dto.administration.RestrictionDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestrictionAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public RestrictionDto convertToDto(Restriction restriction) {
        RestrictionDto res = modelMapper.map(restriction, RestrictionDto.class);
        return res;
    }
}
