package com.petfabula.infrastructure.persistence.jpa.notification.impl;

import com.petfabula.domain.aggregate.notification.entity.SystemNotification;
import com.petfabula.domain.aggregate.notification.respository.SystemNotificationRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.domain.common.paging.JumpableOffsetPage;
import com.petfabula.infrastructure.persistence.jpa.notification.repository.SystemNotificationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class SystemNotificationRepositoryImpl implements SystemNotificationRepository {

    @Autowired
    private SystemNotificationJpaRepository systemNotificationJpaRepository;

    @Override
    public SystemNotification findById(Long id) {
        return systemNotificationJpaRepository.findById(id).orElse(null);
    }

    @Override
    public SystemNotification findLatest() {
        return systemNotificationJpaRepository.findTopByOrderByIdDesc();
    }

    @Override
    public CursorPage<SystemNotification> findAll(Long cursor, int size) {
        Specification<SystemNotification> spec = new Specification<SystemNotification>() {
            @Override
            public Predicate toPredicate(Root<SystemNotification> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(cPred);
                }
                return null;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<SystemNotification> res = systemNotificationJpaRepository.findAll(spec, limit);

        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public JumpableOffsetPage<SystemNotification> findAll(int pageIndex, int pageSize) {
        Pageable sortedById = PageRequest.of(pageIndex, pageSize, Sort.by("id").descending());
        Page<SystemNotification> reasonPage = systemNotificationJpaRepository
                .findAll(sortedById);

        int cnt = (int) reasonPage.getTotalElements();
        return JumpableOffsetPage.of(reasonPage.getContent(), pageIndex, pageSize, cnt);
    }

    @Override
    public SystemNotification save(SystemNotification systemNotification) {
        return systemNotificationJpaRepository.save(systemNotification);
    }

    @Override
    public void remove(SystemNotification systemNotification) {
        systemNotificationJpaRepository.delete(systemNotification);
    }
}
