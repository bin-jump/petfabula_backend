package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.community.AnswerCommentDto;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerCommentAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public AnswerCommentDto convertToDto(AnswerComment answerComment) {
        AnswerCommentDto answerCommentDto =  modelMapper
                .map(answerComment, AnswerCommentDto.class);
        ParticipatorDto participatorDto = answerCommentDto.getParticipator();
        participatorDto.setPhoto(assemblerHelper.completeImageUrl(participatorDto.getPhoto()));
        return answerCommentDto;
    }

    public List<AnswerCommentDto> convertToDtos(List<AnswerComment> answerComments) {

        return answerComments.stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }
}
