package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.common.paging.CursorPage;

public interface SystemNotificationRepository {

    SystemNotification findLatest();

    CursorPage<SystemNotification> findAll(Long cursor, int size);

    SystemNotification save(SystemNotification systemNotification);

    SystemNotification update(SystemNotification systemNotification);

    void remove(SystemNotification systemNotification);
}
