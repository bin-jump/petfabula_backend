package com.petfabula.presentation.facade.assembler.administration;

import com.petfabula.domain.aggregate.administration.entity.Feedback;
import com.petfabula.domain.aggregate.administration.entity.Report;
import com.petfabula.presentation.facade.dto.administration.FeedbackDto;
import com.petfabula.presentation.facade.dto.administration.ReportDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedbackAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public FeedbackDto convertToDto(Feedback feedback) {
        FeedbackDto feedbackDto = modelMapper.map(feedback, FeedbackDto.class);
        return feedbackDto;
    }

    public List<FeedbackDto> convertToDtos(List<Feedback> feedbacks) {
        return feedbacks.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
