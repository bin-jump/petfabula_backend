package com.petfabula.presentation.facade.assembler.administration;

import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.domain.aggregate.administration.entity.ReportReason;
import com.petfabula.presentation.facade.dto.administration.ReportDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReportAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public ReportDto convertToDto(Report report) {
        ReportDto reportDto = modelMapper.map(report, ReportDto.class);
        return reportDto;
    }

    public List<ReportDto> convertToDtos(List<Report> reports) {
        return reports.stream().map(this::convertToDto).collect(Collectors.toList());
    }

}
