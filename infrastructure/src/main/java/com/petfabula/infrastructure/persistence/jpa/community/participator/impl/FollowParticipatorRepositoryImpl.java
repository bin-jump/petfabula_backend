package com.petfabula.infrastructure.persistence.jpa.community.participator.impl;

import com.petfabula.domain.aggregate.community.participator.entity.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.aggregate.community.participator.repository.ParticipatorRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.community.participator.repository.FollowParticipatorJpaRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FollowParticipatorRepositoryImpl implements FollowParticipatorRepository {

    @Autowired
    private FollowParticipatorJpaRepository followParticipatorJpaRepository;

    @Autowired
    private ParticipatorRepository participatorRepository;

    @Override
    public CursorPage<Participator> findFollowed(Long participtorId, Long cursor, int size) {
        Specification<FollowParticipator> spec = new Specification<FollowParticipator>() {
            @Override
            public Predicate toPredicate(Root<FollowParticipator> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("followerId"), participtorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<FollowParticipator> followParticipators = followParticipatorJpaRepository.findAll(spec, limit);

        Long nextCursor = null;
        if (followParticipators.hasNext()) {
            nextCursor = followParticipators.getContent()
                    .get(followParticipators.getContent().size() - 1).getId();
        }

        List<Long> ids = followParticipators.getContent().stream()
                .map(FollowParticipator::getFollowedId).collect(Collectors.toList());
        List<Participator> participators = participatorRepository.findByIds(ids);

        return CursorPage.of(participators, nextCursor, size);
    }

    @Override
    public CursorPage<Participator> findFollower(Long participatorId, Long cursor, int size) {
        Specification<FollowParticipator> spec = new Specification<FollowParticipator>() {
            @Override
            public Predicate toPredicate(Root<FollowParticipator> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("followedId"), participatorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<FollowParticipator> followParticipators = followParticipatorJpaRepository.findAll(spec, limit);

        Long nextCursor = null;
        if (followParticipators.hasNext()) {
            nextCursor = followParticipators.getContent()
                    .get(followParticipators.getContent().size() - 1).getId();
        }

        List<Long> ids = followParticipators.getContent().stream()
                .map(FollowParticipator::getFollowerId).collect(Collectors.toList());
        List<Participator> participators = participatorRepository.findByIds(ids);

        return CursorPage.of(participators, nextCursor, size);
    }

    @Override
    public FollowParticipator find(Long followerId, Long followedId) {
        return followParticipatorJpaRepository.findByFollowerIdAndFollowedId(followerId, followedId);
    }

    @Override
    public FollowParticipator save(FollowParticipator followParticipator) {
        return followParticipatorJpaRepository.save(followParticipator);
    }

    @Override
    public void remove(FollowParticipator followParticipator) {
        followParticipatorJpaRepository.delete(followParticipator);
    }
}
