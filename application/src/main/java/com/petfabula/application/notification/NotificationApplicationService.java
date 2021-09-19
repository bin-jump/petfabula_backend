package com.petfabula.application.notification;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.aggregate.notification.service.*;
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

    @Autowired
    private SystemNotificationService systemNotificationService;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    public SystemNotification getSystemUnreadNotification(Long receiverId) {
        return systemNotificationService.getUnreadNotification(receiverId);
    }

    @Transactional
    public void readAllanswerCommentNotifications(Long receiverId) {
        answerCommentNotificationService.readAll(receiverId);
    }

    @Transactional
    public void readAllParticipatorFollowNotifications(Long receiverId) {
        participatorFollowNotificationService.readAll(receiverId);
    }

    @Transactional
    public void readAllVoteNotifications(Long receiverId) {
        voteNotificationService.readAll(receiverId);
    }

    @Transactional
    public void udpateSetting(Long receiverId, boolean receiveAnswerComment, boolean receiveFollow, boolean receiveUpvote) {
        notificationReceiverService
                .updateSetting(receiverId, receiveAnswerComment, receiveFollow, receiveUpvote);
    }

    @Transactional
    public void readAllSystemNotification(Long receiverId) {
        systemNotificationService.readNotification(receiverId);
    }
}
