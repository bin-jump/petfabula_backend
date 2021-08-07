package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.pet.DisorderRecordDto;
import com.petfabula.presentation.facade.dto.pet.FeedRecordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedRecordAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public FeedRecordDto convertToDto(FeedRecord feedRecord) {
        FeedRecordDto disorderRecordDto = modelMapper.map(feedRecord, FeedRecordDto.class);
        return disorderRecordDto;
    }

    public List<FeedRecordDto> convertToDtos(List<FeedRecord> feedRecords) {
        return feedRecords.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
