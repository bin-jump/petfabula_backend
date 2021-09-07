package com.petfabula.domain.aggregate.community.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostUpdateListener {

    @Autowired
    private PostSearchService searchService;

    @Async
    @TransactionalEventListener
    public void handle(PostUpdated postUpdated) {
        if (postUpdated.getPost().isPrivatePost()) {
            searchService.remove(postUpdated.getPost().getId());
        } else {
            PostSearchItem searchItem = PostSearchItem.of(postUpdated.getPost());
            searchService.index(searchItem);
        }
    }

}
