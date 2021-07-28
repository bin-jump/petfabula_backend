package com.petfabula.application.notification;

import com.petfabula.application.event.PostLikedEvent;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.service.VoteNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationPostLikedListener {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private VoteNotificationService voteNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(PostLikedEvent event) {
        Post post = postRepository.findById(event.getPostId());
        if (post == null || post.getParticipator().getId().equals(event.getParticipatorId())) {
            return;
        }
        voteNotificationService.createNotification(post.getParticipator().getId(),
                event.getParticipatorId(),
                event.getPostId(),
                UpvoteNotification.EntityType.POST,
                UpvoteNotification.ActionType.UPVOTE);
    }
}
