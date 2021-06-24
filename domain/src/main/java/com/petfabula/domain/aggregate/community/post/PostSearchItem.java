package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
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
