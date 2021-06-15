package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.common.domain.DomainEvent;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class PostChanged extends DomainEvent {

    private PostSearchItem postSearchItem;

    public PostChanged(Post post) {
        SearchImageItem coverImage = null;
        if (post.getImages().size() > 0) {
            PostImage cimg = post.getImages().get(0);
            coverImage = new SearchImageItem(cimg.getUrl(),
                    cimg.getWidth(), cimg.getHeight());
        }

        String petCategory = post.getPetCategory() != null ? post.getPetCategory().toString() : null;
        postSearchItem = PostSearchItem.builder()
                .id(post.getId())
                .relatePetId(post.getRelatePetId())
                .content(post.getContent())
                .coverImage(coverImage)
                .version(post.getVersion())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .petCategory(petCategory)
                .createdDate(post.getCreatedDate())
                .participator(ParticipatorSearchItem.of(post.getParticipator()))
                .build();
    }
}
