package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.Post;
import com.petfabula.domain.aggregate.community.post.repository.PostRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepositoryImpl implements PostRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Transactional
    @FilterSoftDelete
    @Override
    public CursorPage<Post> findByParticipatorId(Long participatorId, Long cursor, int size) {

        String q = "select p.id from Post p where (:cursor is null or p.id < :cursor) and p.participator.id = :participatorId order by p.id desc";
        List<Long> ids = entityManager.createQuery(q, Long.class)
                .setParameter("participatorId", participatorId)
                .setParameter("cursor", cursor)
                .setMaxResults(size).getResultList();

        if (ids.size() == 0) {
            return CursorPage.empty(size);
        }

        Long nextCursor = ids.get(ids.size() - 1);
        String cntq = "select count(p) from Post p where (:cursor is null or p.id < :cursor) and p.participator.id = :participatorId order by p.id desc";
        Long cnt =  entityManager.createQuery(cntq, Long.class)
                .setParameter("participatorId", participatorId)
                .setParameter("cursor", nextCursor)
                .getSingleResult();

        List<Post> posts = postJpaRepository.findByIdInOrderByIdDesc(ids);
        return CursorPage.of(posts, cnt > 0, size);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public CursorPage<Post> findByPetId(Long petId, Long cursor, int size) {

        String q = "select p.id from Post p where (:cursor is null or p.id < :cursor) and p.relatePetId = :relatePetId order by p.id desc";
        List<Long> ids = entityManager.createQuery(q, Long.class)
                .setParameter("relatePetId", petId)
                .setParameter("cursor", cursor)
                .setMaxResults(size).getResultList();

        if (ids.size() == 0) {
            return CursorPage.empty(size);
        }

        Long nextCursor = ids.get(ids.size() - 1);
        String cntq = "select count(p) from Post p where (:cursor is null or p.id < :cursor) and p.relatePetId = :relatePetId order by p.id desc";
        Long cnt =  entityManager.createQuery(cntq, Long.class)
                .setParameter("relatePetId", petId)
                .setParameter("cursor", nextCursor)
                .getSingleResult();

        List<Post> posts = postJpaRepository.findByIdInOrderByIdDesc(ids);
        return CursorPage.of(posts, cnt > 0, size);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public List<Post> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return postJpaRepository.findByIdInOrderByIdDesc(ids);
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(post);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public Post findById(Long id) {
        return postJpaRepository.findById(id).orElse(null);
    }

    @Override
    public void remove(Post post) {
        post.markDelete();
        postJpaRepository.save(post);
    }
}
