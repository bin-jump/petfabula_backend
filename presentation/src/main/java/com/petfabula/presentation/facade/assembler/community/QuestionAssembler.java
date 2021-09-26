package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.question.QuestionAnswerSearchItem;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.community.QuestionAnswerSearchDto;
import com.petfabula.presentation.facade.dto.community.QuestionDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class QuestionAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public QuestionDto convertToDto(Question question) {
        QuestionDto questionDto = modelMapper.map(question, QuestionDto.class);
        questionDto.getImages().clear();
        question.getImages().forEach(item -> questionDto.getImages()
                .add(new ImageDto(item.getId(), assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));
        questionDto.getParticipator()
                .setPhoto(assemblerHelper.completeImageUrl(questionDto.getParticipator().getPhoto()));
        return questionDto;
    }

    public List<QuestionDto> convertToDtos(List<Question> questions) {
        return questions.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public QuestionAnswerSearchDto convertSearchDto(QuestionAnswerSearchItem questionAnswerSearchItem) {
        QuestionAnswerSearchDto questionAnswerSearchDto = modelMapper.map(questionAnswerSearchItem, QuestionAnswerSearchDto.class);
        questionAnswerSearchDto.getImages().clear();
        questionAnswerSearchItem.getImages()
                .stream().forEach(item -> {
            ImageDto img = new ImageDto(null, assemblerHelper
                    .completeImageUrl(item.getUrl()),
                    item.getWidth(), item.getHeight());
            questionAnswerSearchDto.getImages().add(img);
        });

        return questionAnswerSearchDto;
    }

    public List<QuestionAnswerSearchDto> convertSearchDtos(List<QuestionAnswerSearchItem> questionAnswerSearchItems) {
        return questionAnswerSearchItems.stream().map(this::convertSearchDto).collect(Collectors.toList());
    }
}
