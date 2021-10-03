package com.petfabula.domain.aggregate.community.post.event;

import com.petfabula.domain.aggregate.community.post.entity.PostTopicCategory;
import com.petfabula.domain.common.domain.DomainEvent;
import lombok.Getter;

@Getter
public class TopicCategoryRemovedEvent extends DomainEvent {

    private PostTopicCategory topicCategory;

    public TopicCategoryRemovedEvent(PostTopicCategory topicCategory) {
        this.topicCategory = topicCategory;
    }
}
