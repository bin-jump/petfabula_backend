package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.PetEventRecord;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.pet.PetEventRecordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PetEventRecordAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public PetEventRecordDto convertToDto(PetEventRecord petEventRecord) {
        PetEventRecordDto petEventRecordDto = modelMapper.map(petEventRecord, PetEventRecordDto.class);
        petEventRecordDto.getImages().clear();
        petEventRecord.getImages().forEach(item -> petEventRecordDto.getImages()
                .add(new ImageDto(item.getId(), assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));
        return petEventRecordDto;
    }

    public List<PetEventRecordDto> convertToDtos(List<PetEventRecord> petEventRecords) {
        return petEventRecords.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
