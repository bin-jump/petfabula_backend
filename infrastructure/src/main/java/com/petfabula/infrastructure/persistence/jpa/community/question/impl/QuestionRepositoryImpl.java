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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class QuestionRepositoryImpl implements QuestionRepository {

    @PersistenceContext
    private EntityManager entityManager;

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

        String q = "select q.id from Question q where (:cursor is null or q.id < :cursor) and q.participator.id = :participatorId order by q.id desc";
        List<Long> ids = entityManager.createQuery(q, Long.class)
                .setParameter("participatorId", participatorId)
                .setParameter("cursor", cursor)
                .setMaxResults(size).getResultList();

        if (ids.size() == 0) {
            return CursorPage.empty(size);
        }

        Long nextCursor = ids.get(ids.size() - 1);
        String cntq = "select count(q) from Question q where (:cursor is null or q.id < :cursor) and q.participator.id = :participatorId order by q.id desc";
        Long cnt =  entityManager.createQuery(cntq, Long.class)
                .setParameter("participatorId", participatorId)
                .setParameter("cursor", nextCursor)
                .getSingleResult();

        List<Question> questions = questionJpaRepository.findByIdInOrderByIdDesc(ids);
        return CursorPage.of(questions, cnt > 0, size);
    }

    @FilterSoftDelete
    @Transactional
    @Override
    public CursorPage<Question> findRecent(Long cursor, int size) {
        String q = "select q.id from Question q where (:cursor is null or q.id < :cursor) order by q.id desc";
        List<Long> ids = entityManager.createQuery(q, Long.class)
                .setParameter("cursor", cursor)
                .setMaxResults(size).getResultList();

        if (ids.size() == 0) {
            return CursorPage.empty(size);
        }

        Long nextCursor = ids.get(ids.size() - 1);
        String cntq = "select count(q) from Question q where (:cursor is null or q.id < :cursor) order by q.id desc";
        Long cnt =  entityManager.createQuery(cntq, Long.class)
                .setParameter("cursor", nextCursor)
                .getSingleResult();

        List<Question> questions = questionJpaRepository.findByIdInOrderByIdDesc(ids);
        return CursorPage.of(questions, cnt > 0, size);
    }
}
