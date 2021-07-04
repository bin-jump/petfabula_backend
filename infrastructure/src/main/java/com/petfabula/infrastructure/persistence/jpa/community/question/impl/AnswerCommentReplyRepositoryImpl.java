package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.AnswerCommentReply;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentReplyRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.AnswerCommentReplyJpaRepository;
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
public class AnswerCommentReplyRepositoryImpl implements AnswerCommentReplyRepository {

    @Autowired
    private AnswerCommentReplyJpaRepository answerCommentReplyJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<AnswerCommentReply> findByComment(Long answerCommentId, Long cursor, int size) {
        Specification<AnswerCommentReply> spec = new Specification<AnswerCommentReply>() {
            @Override
            public Predicate toPredicate(Root<AnswerCommentReply> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.asc(root.get("id")));
                Predicate aPred = cb.equal(root.get("commentId"), answerCommentId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<AnswerCommentReply> res = answerCommentReplyJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public AnswerCommentReply findById(Long id) {
        return answerCommentReplyJpaRepository.findById(id).orElse(null);
    }

    @Override
    public AnswerCommentReply save(AnswerCommentReply reply) {
        return answerCommentReplyJpaRepository.save(reply);
    }

    @Override
    public void remove(AnswerCommentReply reply) {
        reply.markDelete();
        answerCommentReplyJpaRepository.save(reply);
    }
}
