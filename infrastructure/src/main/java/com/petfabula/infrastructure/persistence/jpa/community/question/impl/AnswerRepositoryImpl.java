package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.Answer;
import com.petfabula.domain.aggregate.community.question.repository.AnswerRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.AnswerJpaRepository;
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
public class AnswerRepositoryImpl implements AnswerRepository {

    @Autowired
    private AnswerJpaRepository answerJpaRepository;

    @Override
    public Answer save(Answer answer) {
        return answerJpaRepository.save(answer);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public Answer findById(Long answerId) {
        return answerJpaRepository.findById(answerId).orElse(null);
    }

    @Override
    public List<Answer> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return answerJpaRepository.findByIdIn(ids);
    }

    @Override
    public void remove(Answer answer) {
        answer.markDelete();
        answerJpaRepository.save(answer);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Answer> findRecent(Long cursor, int size) {
        Specification<Answer> spec = new Specification<Answer>() {
            @Override
            public Predicate toPredicate(Root<Answer> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cPred;
                }
                return null;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Answer> res = answerJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Answer> findByQuestionId(Long questionId, Long cursor, int size) {
        Specification<Answer> spec = new Specification<Answer>() {
            @Override
            public Predicate toPredicate(Root<Answer> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("questionId"), questionId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Answer> res = answerJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Answer> findByParticipatorId(Long participatorId, Long cursor, int size) {
        Specification<Answer> spec = new Specification<Answer>() {
            @Override
            public Predicate toPredicate(Root<Answer> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("participator").get("id"), participatorId);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Answer> res = answerJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }
}
