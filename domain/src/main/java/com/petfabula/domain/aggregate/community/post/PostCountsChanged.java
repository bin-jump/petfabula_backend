package com.petfabula.domain.aggregate.community.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostCountsChanged {

    private Integer version;

    private Integer likeCount;

    private Integer commentCount;

    private Integer viewCount;
}
