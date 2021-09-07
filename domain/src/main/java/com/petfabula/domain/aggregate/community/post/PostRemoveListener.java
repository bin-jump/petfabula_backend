package com.petfabula.domain.aggregate.community.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PostRemoveListener {

    @Autowired
    private PostSearchService searchService;

    @Async
    @TransactionalEventListener
    public void handle(PostRemoved postRemoved) {
        searchService.remove(postRemoved.getPost().getId());
    }
}
