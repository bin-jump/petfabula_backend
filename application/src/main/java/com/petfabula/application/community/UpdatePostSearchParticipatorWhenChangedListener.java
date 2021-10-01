package com.petfabula.application.community;

import com.petfabula.application.event.AccountUpdateEvent;
import com.petfabula.domain.aggregate.community.post.PostSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UpdatePostSearchParticipatorWhenChangedListener {

    @Autowired
    private PostSearchService postSearchService;

    @Async
    @TransactionalEventListener
    public void handle(AccountUpdateEvent event) {
        postSearchService
                .updateParticipatorInfo(event.getId(), event.getPhoto());
    }
}
