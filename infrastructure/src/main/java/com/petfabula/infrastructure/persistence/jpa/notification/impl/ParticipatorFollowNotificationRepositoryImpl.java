package com.petfabula.infrastructure.persistence.jpa.notification.impl;

import com.petfabula.domain.aggregate.notification.entity.ParticipatorFollowNotification;
import com.petfabula.domain.aggregate.notification.respository.ParticipatorFollowNotificationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.notification.repository.ParticipatorFollowNotificationJpaRepository;
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
public class ParticipatorFollowNotificationRepositoryImpl implements ParticipatorFollowNotificationRepository {

    @Autowired
    private ParticipatorFollowNotificationJpaRepository participatorFollowNotificationJpaRepository;

    @Override
    public ParticipatorFollowNotification findByAction(Long followerId, Long followeeId) {
        return participatorFollowNotificationJpaRepository
                .findByFollowerIdAndFolloweeId(followerId, followeeId);
    }

    @Override
    public ParticipatorFollowNotification save(ParticipatorFollowNotification followNotification) {
        return participatorFollowNotificationJpaRepository.save(followNotification);
    }

    @Override
    public CursorPage<ParticipatorFollowNotification> findByFolloweeId(Long followeeId, Long cursor, int size) {
        Specification<ParticipatorFollowNotification> spec = new Specification<ParticipatorFollowNotification>() {
            @Override
            public Predicate toPredicate(Root<ParticipatorFollowNotification> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("followeeId"), followeeId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<ParticipatorFollowNotification> res = participatorFollowNotificationJpaRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(ParticipatorFollowNotification followNotification) {
        participatorFollowNotificationJpaRepository.delete(followNotification);
    }
}
