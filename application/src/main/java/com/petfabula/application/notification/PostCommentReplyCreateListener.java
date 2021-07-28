package com.petfabula.application.notification;

import com.petfabula.application.event.PostCommentReplyCreateEvent;
import com.petfabula.domain.aggregate.community.post.entity.PostComment;
import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentReplyRepository;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentRepository;
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
public class PostCommentReplyCreateListener {

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentReplyRepository postCommentReplyRepository;

    @Autowired
    private AnswerCommentNotificationService answerCommentNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(PostCommentReplyCreateEvent event) {
        PostCommentReply commentReply = event.getPostCommentReply();
        Long replyToId = commentReply.getReplyToId();
        // if to comment
        if (replyToId == null) {
            PostComment comment = postCommentRepository.findById(commentReply.getCommentId());
            if (comment == null || comment.getParticipator().getId()
                    .equals(commentReply.getParticipator().getId())) {
                return;
            }
            answerCommentNotificationService.createNotification(comment.getParticipator().getId(),
                    commentReply.getParticipator().getId(),
                    commentReply.getId(),
                    AnswerCommentNotification.EntityType.POST_COMMENT_REPLY,
                    comment.getId(),
                    AnswerCommentNotification.EntityType.POST_COMMENT,
                    AnswerCommentNotification.ActionType.REPLY);
        } else {

            PostCommentReply replyTarget = postCommentReplyRepository.findById(replyToId);
            if (replyTarget == null || replyTarget.getParticipator().getId()
                    .equals(commentReply.getParticipator().getId())) {
                return;
            }
            answerCommentNotificationService.createNotification(replyTarget.getParticipator().getId(),
                    commentReply.getParticipator().getId(),
                    commentReply.getId(),
                    AnswerCommentNotification.EntityType.POST_COMMENT_REPLY,
                    replyTarget.getId(),
                    AnswerCommentNotification.EntityType.POST_COMMENT_REPLY,
                    AnswerCommentNotification.ActionType.REPLY);
        }
    }
}
