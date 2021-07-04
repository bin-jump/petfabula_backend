package com.petfabula.infrastructure.persistence.jpa.community.post.impl;

import com.petfabula.domain.aggregate.community.post.entity.PostCommentReply;
import com.petfabula.domain.aggregate.community.post.repository.PostCommentReplyRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.post.repository.PostCommentReplyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class PostCommentReplyRepositoryImpl implements PostCommentReplyRepository {

    @Autowired
    private PostCommentReplyJpaRepository postCommentReplyJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<PostCommentReply> findByPostComment(Long postCommentId, Long cursor, int size) {
        Specification<PostCommentReply> spec = new Specification<PostCommentReply>() {
            @Override
            public Predicate toPredicate(Root<PostCommentReply> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.asc(root.get("id")));

                Predicate pPred = cb.equal(root.get("commentId"), postCommentId);
                if (cursor != null) {
                    Predicate cPred = cb.greaterThan(root.get("id"), cursor);
                    return cb.and(pPred, cPred);
                }
                return pPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<PostCommentReply> res = postCommentReplyJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public PostCommentReply findById(Long id) {
        return postCommentReplyJpaRepository.findById(id).orElse(null);
    }

    @Override
    public PostCommentReply save(PostCommentReply reply) {
        return postCommentReplyJpaRepository.save(reply);
    }

    @Override
    public void remove(PostCommentReply reply) {
        reply.markDelete();
        postCommentReplyJpaRepository.save(reply);
    }
}
