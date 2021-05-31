package com.petfabula.infrastructure.persistence.jpa.community.impl;

import com.petfabula.domain.aggregate.community.entity.Post;
import com.petfabula.domain.aggregate.community.repository.TimelineRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class TimeLineRepositoryImpl implements TimelineRepository {

    @PersistenceContext
    private EntityManager em;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Post> findFollowedByParticipatorId(Long participatorId, Long cursor, int size) {

        String q = "select p from Post p inner join FollowParticipator f on f.followed = p.participator " +
                "where f.followParticipatorId.followerId = :participatorId and (:cursor is null or p.id < :cursor)" +
                " order by p.id desc ";

        TypedQuery<Post> query = em.createQuery(q, Post.class);
        query.setParameter("participatorId", participatorId);
        query.setParameter("cursor", cursor);
        query.setFirstResult(0);
        query.setMaxResults(size);


        String cq = "select count(p) from Post p inner join FollowParticipator f on f.followed = p.participator " +
                "where f.followParticipatorId.followerId = :participatorId and (:cursor is null or p.id < :cursor)" +
                " order by p.id desc ";
        TypedQuery<Long> countQuery = em.createQuery(cq, Long.class);
        countQuery.setParameter("participatorId", participatorId);
        countQuery.setParameter("cursor", cursor);
        Long num = countQuery.getSingleResult();

        boolean hasMore = num.longValue() > size;
        List<Post> res = query.getResultList();
        return CursorPage.of(res, hasMore, size);
    }
}
