package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.community.ParticipatorDto;
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

    @Autowired
    private AssemblerHelper assemblerHelper;

    public PostCommentReplyDto convertToDto(PostCommentReply postCommentReply) {
        PostCommentReplyDto postCommentReplyDto =  modelMapper.map(postCommentReply, PostCommentReplyDto.class);
        ParticipatorDto participatorDto = postCommentReplyDto.getParticipator();
        participatorDto.setPhoto(assemblerHelper.completeImageUrl(participatorDto.getPhoto()));
        return postCommentReplyDto;
    }

    public List<PostCommentReplyDto> convertToDtos(List<PostCommentReply> postComments) {

        return postComments.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
