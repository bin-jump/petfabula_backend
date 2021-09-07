package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class PostRemoved extends DomainEvent {

    private Post post;

    public PostRemoved(Post post) {
        this.post = post;
    }
}
