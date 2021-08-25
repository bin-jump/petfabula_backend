package com.petfabula.presentation.facade.assembler.pet;

import com.petfabula.domain.aggregate.pet.entity.MedicalRecord;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.pet.MedicalRecordDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MedicalRecordAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public MedicalRecordDto convertToDto(MedicalRecord medicalRecord) {
        MedicalRecordDto medicalRecordDto = modelMapper.map(medicalRecord, MedicalRecordDto.class);
        medicalRecordDto.getImages().clear();
        medicalRecord.getImages().forEach(item -> medicalRecordDto.getImages()
                .add(new ImageDto(item.getId(), assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));
        return medicalRecordDto;
    }

    public List<MedicalRecordDto> convertToDtos(List<MedicalRecord> medicalRecords) {
        return medicalRecords.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
