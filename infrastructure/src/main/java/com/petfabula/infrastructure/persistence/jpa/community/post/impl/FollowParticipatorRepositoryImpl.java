package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.participator.FollowParticipator;
import com.petfabula.domain.aggregate.community.participator.entity.Participator;
import com.petfabula.domain.aggregate.community.participator.repository.FollowParticipatorRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.FollowParticipatorJpaRepository;
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

    @Override
    public CursorPage<Participator> findFollowed(Long participtorId, Long cursor, int size) {
        Specification<FollowParticipator> spec = new Specification<FollowParticipator>() {
            @Override
            public Predicate toPredicate(Root<FollowParticipator> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("followed_id").get("id")));

                Predicate aPred = cb.equal(root.get("follower_id").get("id"), participtorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("followed_id").get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<FollowParticipator> res = followParticipatorJpaRepository.findAll(spec, limit);
        List<Participator> authors = res.stream().map(FollowParticipator::getFollowed)
                .collect(Collectors.toList());


        return CursorPage.of(authors, res.hasNext(), size);
    }

    @Override
    public FollowParticipator find(Long followerId, Long followedId) {
        return followParticipatorJpaRepository.find(followerId, followedId);
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
