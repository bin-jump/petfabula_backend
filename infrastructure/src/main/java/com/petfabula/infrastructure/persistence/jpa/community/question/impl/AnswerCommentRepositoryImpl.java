package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.AnswerComment;
import com.petfabula.domain.aggregate.community.question.repository.AnswerCommentRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.AnswerCommentJpaRepository;
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
import java.util.ArrayList;
import java.util.List;

@Repository
public class AnswerCommentRepositoryImpl implements AnswerCommentRepository {

    @Autowired
    private AnswerCommentJpaRepository answerCommentJpaRepository;

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<AnswerComment> findByAnswerId(Long answerId, Long cursor, int size) {
        Specification<AnswerComment> spec = new Specification<AnswerComment>() {
            @Override
            public Predicate toPredicate(Root<AnswerComment> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.asc(root.get("id")));
                Predicate aPred = cb.equal(root.get("answerId"), answerId);
                if (cursor != null) {
                    Predicate cPred = cb.greaterThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<AnswerComment> res = answerCommentJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public List<AnswerComment> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return answerCommentJpaRepository.findByIdIn(ids);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public AnswerComment findById(Long id) {
        return answerCommentJpaRepository.findById(id).orElse(null);
    }

    @Override
    public AnswerComment save(AnswerComment answerComment) {
        return answerCommentJpaRepository.save(answerComment);
    }

    @Override
    public void remove(AnswerComment answerComment) {
        answerComment.markDelete();
        answerCommentJpaRepository.save(answerComment);
    }
}
