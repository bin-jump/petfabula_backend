package com.petfabula.domain.aggregate.community.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostCreateListener {

    @Autowired
    private PostSearchService searchService;

    @Async
    @TransactionalEventListener
    public void handle(PostCreated postCreated) {
        if (postCreated.getPost().isPrivatePost()) {
            return;
        }
        PostSearchItem searchItem = PostSearchItem.of(postCreated.getPost());
        searchService.index(searchItem);
    }

}
