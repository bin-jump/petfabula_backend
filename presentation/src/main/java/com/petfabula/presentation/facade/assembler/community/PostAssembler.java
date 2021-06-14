package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.community.PostDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostAssembler {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AssemblerHelper assemblerHelper;

    public PostDto convertToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.getImages().clear();
        post.getImages().forEach(item -> postDto.getImages()
                .add(assemblerHelper.completeImageUrl(item.getUrl())));

        return postDto;
    }

    public List<PostDto> convertToDtos(List<Post> posts) {

        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
