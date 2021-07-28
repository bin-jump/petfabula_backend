package com.petfabula.application.notification;

import com.petfabula.application.event.AnswerCommentCreateEvent;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentRepository;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.service.AnswerCommentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationAnswerCommentCreateListener {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerCommentRepository answerCommentRepository;

    @Autowired
    private AnswerCommentNotificationService answerCommentNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(AnswerCommentCreateEvent event) {
        AnswerComment answerComment = event.getAnswerComment();
        Long replyToId = answerComment.getReplyTo();

        // if comment to answer
        if (replyToId == null) {
            Answer answer = answerRepository.findById(answerComment.getAnswerId());
            if (answer == null || answerComment.getParticipator().getId()
                    .equals(answer.getParticipator().getId())) {
                return;
            }
            answerCommentNotificationService.createNotification(answer.getParticipator().getId(),
                    answerComment.getParticipator().getId(),
                    answerComment.getId(),
                    AnswerCommentNotification.EntityType.ANSWER_COMMENT,
                    answer.getId(),
                    AnswerCommentNotification.EntityType.ANSWER,
                    AnswerCommentNotification.ActionType.COMMENT);
        } else {
            // if reply to answer comment
            AnswerComment replyTarget = answerCommentRepository.findById(replyToId);
            if (replyTarget == null || answerComment.getParticipator().getId()
                    .equals(replyTarget.getParticipator().getId())) {
                return;
            }
            answerCommentNotificationService.createNotification(replyTarget.getParticipator().getId(),
                    answerComment.getParticipator().getId(),
                    answerComment.getId(),
                    AnswerCommentNotification.EntityType.ANSWER_COMMENT,
                    replyTarget.getId(),
                    AnswerCommentNotification.EntityType.ANSWER_COMMENT,
                    AnswerCommentNotification.ActionType.REPLY);
        }



    }
}
