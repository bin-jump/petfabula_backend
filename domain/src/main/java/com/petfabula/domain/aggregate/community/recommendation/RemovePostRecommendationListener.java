package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.PostRemoved;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


@Component
public class RemovePostRecommendationListener {

    @Autowired
    private PostRecommendationService postRecommendationService;

    @Async
    @TransactionalEventListener
    public void handle(PostRemoved postRemoved) {
        postRecommendationService.remove(postRemoved.getPost().getId());
    }
}
