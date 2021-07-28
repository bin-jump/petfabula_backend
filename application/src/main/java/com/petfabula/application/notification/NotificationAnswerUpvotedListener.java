package com.petfabula.application.notification;

import com.petfabula.application.event.AnswerUpvotedEvent;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.service.VoteNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationAnswerUpvotedListener {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private VoteNotificationService voteNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(AnswerUpvotedEvent event) {
        Answer answer = answerRepository.findById(event.getAnswerId());
        if (answer == null || answer.getParticipator().getId().equals(event.getParticipatorId())) {
            return;
        }
        voteNotificationService.createNotification(answer.getParticipator().getId(),
                event.getParticipatorId(),
                event.getAnswerId(),
                UpvoteNotification.EntityType.ANSWER,
                UpvoteNotification.ActionType.UPVOTE);
    }
}
