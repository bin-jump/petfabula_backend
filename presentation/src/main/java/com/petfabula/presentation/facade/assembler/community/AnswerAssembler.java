package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.community.AnswerDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public AnswerDto convertToDto(Answer answer) {
        AnswerDto answerDto = modelMapper.map(answer, AnswerDto.class);
        answerDto.getImages().clear();
        answer.getImages().forEach(item -> answerDto.getImages()
                .add(new ImageDto(assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));

        return answerDto;
    }

    public List<AnswerDto> convertToDtos(List<Answer> answers) {
        return answers.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
