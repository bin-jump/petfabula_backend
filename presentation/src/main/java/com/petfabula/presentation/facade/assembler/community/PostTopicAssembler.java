package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.presentation.facade.dto.community.PostTopicCategoryDto;
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

    public List<PostTopicDto> convertToTopicDtos(List<PostTopic> topics) {
        return topics.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PostTopicCategoryDto convertToDto(PostTopicCategory postTopicCategory) {
        PostTopicCategoryDto res = modelMapper.map(postTopicCategory, PostTopicCategoryDto.class);
        return res;
    }

    public List<PostTopicCategoryDto> convertToTopicCategoryDtos(List<PostTopicCategory> categories) {
        return categories.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
