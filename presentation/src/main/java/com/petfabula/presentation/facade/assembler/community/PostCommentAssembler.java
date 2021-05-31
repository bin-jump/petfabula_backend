package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.entity.PostComment;
import com.petfabula.presentation.facade.dto.community.PostCommentDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostCommentAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PostCommentDto convertToDto(PostComment postComment) {
        PostCommentDto postCommentDto =  modelMapper.map(postComment, PostCommentDto.class);
        return postCommentDto;
    }

    public List<PostCommentDto> convertToDtos(List<PostComment> postComments) {

        return postComments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
