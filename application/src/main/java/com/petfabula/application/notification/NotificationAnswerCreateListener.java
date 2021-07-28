package com.petfabula.application.notification;

import com.petfabula.application.event.AnswerCreateEvent;
import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.service.AnswerCommentNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationAnswerCreateListener {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerCommentNotificationService answerCommentNotificationService;

    @Async
    @Transactional
    @TransactionalEventListener
    public void handle(AnswerCreateEvent event) {
        Answer answer = event.getAnswer();
        Question question = questionRepository.findById(answer.getQuestionId());
        if (question == null || question.getParticipator().getId()
                .equals(answer.getParticipator().getId())) {
            return;
        }
        answerCommentNotificationService.createNotification(question.getParticipator().getId(),
                answer.getParticipator().getId(),
                answer.getId(),
                AnswerCommentNotification.EntityType.ANSWER,
                answer.getQuestionId(),
                AnswerCommentNotification.EntityType.QUESTION,
                AnswerCommentNotification.ActionType.ANSWER);
    }
}
