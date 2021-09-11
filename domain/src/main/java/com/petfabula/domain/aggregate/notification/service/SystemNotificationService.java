package com.petfabula.domain.aggregate.notification.service;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.respository.NotificationReceiverRepository;
import com.petfabula.domain.aggregate.notification.respository.SystemNotificationRepository;
import com.petfabula.domain.common.CommonMessageKeys;
import com.petfabula.domain.exception.InvalidOperationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class SystemNotificationService {

    @Autowired
    private SystemNotificationRepository systemNotificationRepository;

    @Autowired
    private NotificationReceiverRepository notificationReceiverRepository;

    @Autowired
    private NotificationIdGenerator notificationIdGenerator;


    public SystemNotification create(Long senderId, String title, String content) {
        Long id = notificationIdGenerator.nextId();
        SystemNotification systemNotification =
                new SystemNotification(id, senderId, title, content);
        return systemNotificationRepository.save(systemNotification);
    }

    public SystemNotification update(Long notificationId, String title, String content) {
        SystemNotification systemNotification =
                systemNotificationRepository.findById(notificationId);

        if (systemNotification == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        systemNotification.setTitle(title);
        systemNotification.setContent(content);

        return systemNotificationRepository.save(systemNotification);
    }

    public SystemNotification remove(Long notificationId) {
        SystemNotification systemNotification =
                systemNotificationRepository.findById(notificationId);

        if (systemNotification == null) {
            return null;
        }

        systemNotificationRepository.remove(systemNotification);
        return systemNotification;
    }

    public SystemNotification getUnreadNotification(Long receiverId) {
        NotificationReceiver receiver = notificationReceiverRepository.findById(receiverId);
        if (receiver == null) {
            throw new InvalidOperationException(CommonMessageKeys.CANNOT_PROCEED);
        }

        SystemNotification systemNotification = systemNotificationRepository.findLatest();
        if (systemNotification != null && systemNotification.getCreatedDate()
                .isAfter(receiver.getSystemLastReadTime())) {
            return systemNotification;
        }
        return null;
    }

    public void readNotification(Long receiverId) {
        notificationReceiverRepository
                .updateSystemNotificationReadTime(receiverId, Instant.now());
    }
}
