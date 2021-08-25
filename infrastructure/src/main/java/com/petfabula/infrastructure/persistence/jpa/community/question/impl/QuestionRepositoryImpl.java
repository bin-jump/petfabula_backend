package com.petfabula.infrastructure.persistence.jpa.community.question.impl;

import com.petfabula.domain.aggregate.community.question.entity.Question;
import com.petfabula.domain.aggregate.community.question.repository.QuestionRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.community.question.repository.QuestionJpaRepository;
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
public class QuestionRepositoryImpl implements QuestionRepository {

    @Autowired
    private QuestionJpaRepository questionJpaRepository;

    @Override
    public Question save(Question question) {
        return questionJpaRepository.save(question);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public Question findById(Long id) {
        return questionJpaRepository.findById(id).orElse(null);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public List<Question> findByIds(List<Long> ids) {
        if (ids.size() == 0) {
            return new ArrayList<>();
        }
        return questionJpaRepository.findByIdInOrderByIdDesc(ids);
    }

    @Override
    public void remove(Question question) {
        question.markDelete();
        questionJpaRepository.save(question);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Question> findByParticipatorId(Long participatorId, Long cursor, int size) {
        Specification<Question> spec = new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
        Page<Question> res = questionJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Question> findRecent(Long cursor, int size) {
        Specification<Question> spec = new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cPred;
                }
                return null;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Question> res = questionJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Question> findUnanswered(Long cursor, int size) {
        Specification<Question> spec = new Specification<Question>() {
            @Override
            public Predicate toPredicate(Root<Question> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.desc(root.get("id")));
                Predicate aPred = cb.equal(root.get("answerCount"), 0);
                if (cursor != null) {
                    Predicate cPred = cb.lessThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<Question> res = questionJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }
}
