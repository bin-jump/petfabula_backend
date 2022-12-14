package com.petfabula.domain.aggregate.community.post;

import com.petfabula.domain.aggregate.community.participator.ParticipatorSearchItem;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostImage;
import com.petfabula.domain.common.domain.DomainEvent;
import com.petfabula.domain.common.search.SearchImageItem;
import lombok.Data;
import lombok.Getter;

@Getter
public class PostCreated extends DomainEvent {

    private Post post;

    public PostCreated(Post post) {
        this.post = post;
    }
}
