package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.pet.DisorderRecordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DisorderRecordAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public DisorderRecordDto convertToDto(DisorderRecord disorderRecord) {
        DisorderRecordDto disorderRecordDto = modelMapper.map(disorderRecord, DisorderRecordDto.class);
        disorderRecordDto.getImages().clear();
        disorderRecord.getImages().forEach(item -> disorderRecordDto.getImages()
                .add(new ImageDto(assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));
        return disorderRecordDto;
    }

    public List<DisorderRecordDto> convertToDtos(List<DisorderRecord> disorderRecords) {
        return disorderRecords.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
