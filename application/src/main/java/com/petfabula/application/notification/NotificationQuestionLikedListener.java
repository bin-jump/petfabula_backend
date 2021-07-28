package com.petfabula.application.notification;

import com.petfabula.application.event.QuestionLikedEvent;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.service.VoteNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationQuestionLikedListener {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private VoteNotificationService voteNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(QuestionLikedEvent event) {
        Question question = questionRepository.findById(event.getQuestionId());
        if (question == null || question.getParticipator().getId().equals(event.getParticipatorId())) {
            return;
        }
        voteNotificationService.createNotification(question.getParticipator().getId(),
                event.getParticipatorId(),
                event.getQuestionId(),
                UpvoteNotification.EntityType.QUESTION,
                UpvoteNotification.ActionType.UPVOTE);
    }
}
