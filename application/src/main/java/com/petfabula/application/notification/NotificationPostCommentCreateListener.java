package com.petfabula.application.notification;

import com.petfabula.application.event.PostCommentCreateEvent;
import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.service.AnswerCommentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationPostCommentCreateListener {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AnswerCommentNotificationService answerCommentNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(PostCommentCreateEvent event) {
        PostComment comment = event.getPostComment();
        Post post = postRepository.findById(comment.getPostId());
        if (post == null || post.getParticipator().getId()
                .equals(comment.getParticipator().getId())) {
            return;
        }
        answerCommentNotificationService.createNotification(post.getParticipator().getId(),
                comment.getParticipator().getId(),
                comment.getId(),
                AnswerCommentNotification.EntityType.POST_COMMENT,
                comment.getPostId(),
                AnswerCommentNotification.EntityType.POST,
                AnswerCommentNotification.ActionType.COMMENT);
    }
}
