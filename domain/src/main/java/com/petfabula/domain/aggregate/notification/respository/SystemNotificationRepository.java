package com.petfabula.domain.aggregate.notification.respository;

import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.common.paging.JumpableOffsetPage;

public interface SystemNotificationRepository {

    SystemNotification findById(Long id);

    SystemNotification findLatest();

    CursorPage<SystemNotification> findAll(Long cursor, int size);

    JumpableOffsetPage<SystemNotification> findAll(int pageIndex, int pageSize);

    SystemNotification save(SystemNotification systemNotification);

    void remove(SystemNotification systemNotification);
}
