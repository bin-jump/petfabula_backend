package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PostSearchItem {

    public PostSearchItem(Long id, Long relatePetId,
                          String content, String coverImage, ParticipatorSearchItem user) {
        this.id = id;
        this.relatePetId = relatePetId;
        this.content = content;
        this.coverImage = coverImage;
        this.user = user;
    }

    private Long id;

    private Long relatePetId;

    private String content;

    private String coverImage;

    private Long version;

    private Integer likeCount;

    private Integer commentCount;

    private Integer viewCount;

    private Instant createdDate;

    private ParticipatorSearchItem user;
}
