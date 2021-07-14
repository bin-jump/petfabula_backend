package com.petfabula.domain.aggregate.community.recommendation;

import com.petfabula.domain.aggregate.community.post.PostCreated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreatePostRecommendationListener {

    @Autowired
    private PostRecommendationService postRecommendationService;

    @Async
    @TransactionalEventListener
    public void handle(PostCreated postCreated) {
        postRecommendationService.addNewPost(postCreated.getPost());
    }
}
