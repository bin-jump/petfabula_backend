package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Data;
import lombok.Getter;

@Getter
public class PostCreated extends DomainEvent {

    private PostSearchItem postSearchItem;

    public PostCreated(Post post) {
        String coverImage = null;
        if (post.getImages().size() > 0) {
            coverImage = post.getImages().get(0).getUrl();
        }

        ParticipatorSearchItem user = ParticipatorSearchItem.builder()
                .id(post.getParticipator().getId())
                .name(post.getParticipator().getName())
                .photo(post.getParticipator().getPhoto())
                .build();

        postSearchItem = PostSearchItem.builder()
                .id(post.getId())
                .relatePetId(post.getRelatePetId())
                .content(post.getContent())
                .coverImage(coverImage)
                .version(post.getVersion())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .createdDate(post.getCreatedDate())
                .user(user)
                .build();
    }
}
