package com.petfabula.infrastructure.persistence.jpa.notification.impl;

import com.petfabula.domain.aggregate.notification.entity.AnswerCommentNotification;
import com.petfabula.domain.aggregate.notification.respository.AnswerCommentNotificationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.notification.repository.AnswerCommentNotificationJpaRepository;
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
public class AnswerCommentNotificationRepositoryImpl implements AnswerCommentNotificationRepository {

    @Autowired
    private AnswerCommentNotificationJpaRepository answerCommentNotificationJpaRepository;

    @Override
    public AnswerCommentNotification findByAction(Long entityId,
                                                  AnswerCommentNotification.EntityType entityType,
                                                  Long targetEntityId,
                                                  AnswerCommentNotification.EntityType targetEntityType) {
        return answerCommentNotificationJpaRepository
                .findByEntityIdAndEntityTypeAndTargetEntityIdAndTargetEntityType(entityId, entityType, targetEntityId, targetEntityType);
    }

    @Override
    public AnswerCommentNotification save(AnswerCommentNotification answerCommentNotification) {
        return answerCommentNotificationJpaRepository.save(answerCommentNotification);
    }

    @Override
    public CursorPage<AnswerCommentNotification> findByOwnerId(Long ownerId, Long cursor, int size) {
        Specification<AnswerCommentNotification> spec = new Specification<AnswerCommentNotification>() {
            @Override
            public Predicate toPredicate(Root<AnswerCommentNotification> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        Page<AnswerCommentNotification> res = answerCommentNotificationJpaRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(AnswerCommentNotification answerCommentNotification) {
        answerCommentNotificationJpaRepository.delete(answerCommentNotification);
    }
}
