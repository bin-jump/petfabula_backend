package com.petfabula.infrastructure.persistence.jpa.notification.repository;

import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ParticipatorFollowNotificationJpaRepository extends JpaRepository<ParticipatorFollowNotification, Long>, JpaSpecificationExecutor {

    ParticipatorFollowNotification findByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
