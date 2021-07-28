package com.petfabula.infrastructure.persistence.jpa.notification.impl;

import com.petfabula.domain.aggregate.notification.entity.UpvoteNotification;
import com.petfabula.domain.aggregate.notification.respository.UpvoteNotificationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.notification.repository.UpvoteJpaNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class UpvoteNotificationRepositoryImpl implements UpvoteNotificationRepository {

    @Autowired
    private UpvoteJpaNotificationRepository upvoteJpaNotificationRepository;

    @Override
    public UpvoteNotification findByAction(Long actorId, Long entityId, UpvoteNotification.ActionType actionType,
                                           UpvoteNotification.EntityType entityType) {
        return upvoteJpaNotificationRepository
                .findByActorIdAndTargetEntityIdAndActionTypeAndTargetEntityType(actorId, entityId, actionType, entityType);
    }

    @Override
    public UpvoteNotification save(UpvoteNotification upvoteNotification) {
        return upvoteJpaNotificationRepository.save(upvoteNotification);
    }

    @Override
    public CursorPage<UpvoteNotification> findByOwnerId(Long ownerId, Long cursor, int size) {
        Specification<UpvoteNotification> spec = new Specification<UpvoteNotification>() {
            @Override
            public Predicate toPredicate(Root<UpvoteNotification> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("ownerId"), ownerId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<UpvoteNotification> res = upvoteJpaNotificationRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(UpvoteNotification upvoteNotification) {
        upvoteJpaNotificationRepository.delete(upvoteNotification);
    }
}
