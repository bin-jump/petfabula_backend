package com.petfabula.infrastructure.persistence.jpa.notification.repository;

import com.petfabula.domain.aggregate.notification.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;

public interface NotificationReceiverJpaRepository extends JpaRepository<NotificationReceiver, Long> {

    @Modifying
    @Query("update NotificationReceiver r set r.answerCommentUnreadCount = r.answerCommentUnreadCount + 1 where r.id = :id")
    void increateAnswerCommentUnreadCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.participatorFollowUnreadCount = r.participatorFollowUnreadCount + 1 where r.id = :id")
    void increateParticipatorFollowUnredCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.upvoteUnreadCount = r.upvoteUnreadCount + 1 where r.id = :id")
    void increateVpvoteUnreadtCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.answerCommentUnreadCount = 0 where r.id = :id")
    void resetAnswerCommentUnreadCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.participatorFollowUnreadCount = 0 where r.id = :id")
    void resetParticipatorFollowUnreadCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.upvoteUnreadCount = 0 where r.id = :id")
    void resetVpvoteUnreadtCount(@Param(value = "id") Long id);

    @Modifying
    @Query("update NotificationReceiver r set r.systemLastReadTime = :time where r.id = :id")
    void updateSystemNotificationReadTime(@Param(value = "id") Long id, @Param(value = "time") Instant time);
}
