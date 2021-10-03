package com.petfabula.domain.aggregate.community.post.event;

import com.petfabula.domain.aggregate.community.post.entity.PostTopic;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class TopicRemovedEvent extends DomainEvent {

    private PostTopic topic;

    public TopicRemovedEvent(PostTopic topic) {
        this.topic = topic;
    }
}
