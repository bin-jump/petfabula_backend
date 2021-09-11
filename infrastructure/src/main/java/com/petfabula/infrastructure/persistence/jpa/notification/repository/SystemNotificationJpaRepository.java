package com.petfabula.infrastructure.persistence.jpa.notification.repository;

import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SystemNotificationJpaRepository extends JpaRepository<SystemNotification, Long>, JpaSpecificationExecutor {

    SystemNotification findTopByOrderByIdDesc();
}
