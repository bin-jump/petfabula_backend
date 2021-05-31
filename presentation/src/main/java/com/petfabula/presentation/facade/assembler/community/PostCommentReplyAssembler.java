package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.entity.PostCommentReply;
import com.petfabula.presentation.facade.dto.community.PostCommentReplyDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostCommentReplyAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public PostCommentReplyDto convertToDto(PostCommentReply postCommentReply) {
        PostCommentReplyDto postCommentReplyDto =  modelMapper.map(postCommentReply, PostCommentReplyDto.class);
        return postCommentReplyDto;
    }

    public List<PostCommentReplyDto> convertToDtos(List<PostCommentReply> postComments) {

        return postComments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
