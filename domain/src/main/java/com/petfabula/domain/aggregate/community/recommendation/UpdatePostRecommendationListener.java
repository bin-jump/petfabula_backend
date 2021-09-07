package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.PostRemoved;
import com.petfabula.domain.aggregate.community.post.PostUpdated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UpdatePostRecommendationListener {

    @Autowired
    private PostRecommendationService postRecommendationService;

    @Async
    @TransactionalEventListener
    public void handle(PostUpdated postUpdated) {
        if (postUpdated.getPost().isPrivatePost()) {
            postRecommendationService.remove(postUpdated.getPost().getId());
        } else {
            postRecommendationService.addNewPost(postUpdated.getPost());
        }
    }
}
