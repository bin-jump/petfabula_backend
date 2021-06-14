package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.presentation.facade.dto.community.PostTopicDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostTopicAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PostTopicDto convertToDto(PostTopic postTopic) {
        PostTopicDto postTopicDto = modelMapper.map(postTopic, PostTopicDto.class);
        return postTopicDto;
    }

    public List<PostTopicDto> convertToDtos(List<PostTopic> postComments) {

        return postComments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
