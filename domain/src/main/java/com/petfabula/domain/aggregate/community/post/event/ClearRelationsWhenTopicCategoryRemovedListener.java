package com.petfabula.domain.aggregate.community.post.event;

import com.petfabula.domain.aggregate.community.post.repository.PostTopicRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ClearRelationsWhenTopicCategoryRemovedListener {

    @Autowired
    private PostTopicRelationRepository postTopicRelationRepository;

    @EventListener
    public void handle(TopicCategoryRemovedEvent categoryRemovedEvent) {

        postTopicRelationRepository
                .removeByTopicCategoryId(categoryRemovedEvent.getTopicCategory().getId());
    }
}
