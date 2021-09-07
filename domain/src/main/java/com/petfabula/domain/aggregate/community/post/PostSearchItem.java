package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchItem {

    public static PostSearchItem of(Post post ) {
        SearchImageItem coverImage = null;
        if (post.getImages().size() > 0) {
            PostImage cimg = post.getImages().get(0);
            coverImage = new SearchImageItem(cimg.getUrl(),
                    cimg.getWidth(), cimg.getHeight());
        }

        ParticipatorSearchItem user = ParticipatorSearchItem.builder()
                .id(post.getParticipator().getId())
                .name(post.getParticipator().getName())
                .photo(post.getParticipator().getPhoto())
                .build();

        String petCategory = post.getPetCategory() != null ? post.getPetCategory().toString() : null;
        PostSearchItem postSearchItem = PostSearchItem.builder()
                .id(post.getId())
                .relatePetId(post.getRelatePetId())
                .content(post.getContent())
                .coverImage(coverImage)
                .version(post.getVersion())
                .likeCount(post.getLikeCount())
                .collectCount(post.getCollectCount())
                .commentCount(post.getCommentCount())
                .viewCount(post.getViewCount())
                .petCategory(petCategory)
                .createdDate(post.getCreatedDate())
                .participator(user)
                .build();

        return postSearchItem;
    }

    private Long id;

    private Long relatePetId;

    private String content;

    private SearchImageItem coverImage;

    private Long version;

    private Integer likeCount;

    private Integer collectCount;

    private Integer commentCount;

    private Integer viewCount;

    private String petCategory;

    private Instant createdDate;

    private ParticipatorSearchItem participator;

}
