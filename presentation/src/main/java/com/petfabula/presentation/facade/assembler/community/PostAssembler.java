package com.petfabula.presentation.facade.assembler.community;

import com.petfabula.domain.aggregate.community.post.PostSearchItem;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.presentation.facade.assembler.AssemblerHelper;
import com.petfabula.presentation.facade.dto.ImageDto;
import com.petfabula.presentation.facade.dto.community.PostDto;
import com.petfabula.presentation.facade.dto.community.PostImageDto;
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
                .add(new ImageDto(item.getId(), assemblerHelper
                        .completeImageUrl(item.getUrl()), item.getWidth(), item.getHeight())));

        return postDto;
    }

    public List<PostDto> convertToDtos(List<Post> posts) {
        return posts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public PostDto convertSearchDto(PostSearchItem postSearchItem) {
        PostDto postDto = modelMapper.map(postSearchItem, PostDto.class);
        if (postSearchItem.getCoverImage() != null) {
            ImageDto coverImage = new ImageDto(null, assemblerHelper
                    .completeImageUrl(postSearchItem.getCoverImage().getUrl()),
                    postSearchItem.getCoverImage().getWidth(),
                    postSearchItem.getCoverImage().getHeight());
            postDto.getImages().add(coverImage);
        }
        return postDto;
    }

    public List<PostDto> convertSearchDtos(List<PostSearchItem> postSearchItems) {
        return postSearchItems.stream().map(this::convertSearchDto).collect(Collectors.toList());
    }

    public PostImageDto convertPostImageDto(PostImage postImages) {
        PostImageDto postImageDto = modelMapper.map(postImages, PostImageDto.class);
        postImageDto.setUrl(assemblerHelper.completeImageUrl(postImages.getUrl()));
        return postImageDto;
    }

    public List<PostImageDto> convertPostImageDtos(List<PostImage> postImages) {
        return postImages.stream().map(this::convertPostImageDto).collect(Collectors.toList());
    }
}
