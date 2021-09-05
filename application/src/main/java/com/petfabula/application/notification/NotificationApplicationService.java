package com.petfabula.application.notification;

import com.petfabula.domain.aggregate.notification.service.AnswerCommentNotificationService;
import com.petfabula.domain.aggregate.notification.service.NotificationReceiverService;
import com.petfabula.domain.aggregate.notification.service.ParticipatorFollowNotificationService;
import com.petfabula.domain.aggregate.notification.service.VoteNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationApplicationService {

    @Autowired
    private AnswerCommentNotificationService answerCommentNotificationService;

    @Autowired
    private ParticipatorFollowNotificationService participatorFollowNotificationService;

    @Autowired
    private VoteNotificationService voteNotificationService;

    @Autowired
    private NotificationReceiverService notificationReceiverService;

    @Transactional
    public void readAllanswerCommentNotifications(Long receiverId) {
        answerCommentNotificationService.readAll(receiverId);
    }

    @Transactional
    public void readAllParticipatorFollowNotifications(Long receiverId) {
        participatorFollowNotificationService.readAll(receiverId);
    }

    @Transactional
    public void readAllVoteNotificationServices(Long receiverId) {
        voteNotificationService.readAll(receiverId);
    }

    @Transactional
    public void udpateSetting(Long receiverId, boolean receiveAnswerComment, boolean receiveFollow, boolean receiveUpvote) {
        notificationReceiverService
                .updateSetting(receiverId, receiveAnswerComment, receiveFollow, receiveUpvote);
    }
}
