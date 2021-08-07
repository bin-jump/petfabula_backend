package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.WeightRecord;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.pet.WeightRecordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WeightRecordAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public WeightRecordDto convertToDto(WeightRecord weightRecord) {
        WeightRecordDto medicalRecordDto = modelMapper.map(weightRecord, WeightRecordDto.class);
        return medicalRecordDto;
    }

    public List<WeightRecordDto> convertToDtos(List<WeightRecord> weightRecords) {
        return weightRecords.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
