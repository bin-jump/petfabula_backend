package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.DisorderRecord;
import com.petfabula.domain.aggregate.pet.respository.DisorderRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.annotation.FilterSoftDelete;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.DisorderRecordJpaRepository;
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
import java.time.Instant;
import java.util.List;

@Repository
public class DisorderRecordRepositoryImpl implements DisorderRecordRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DisorderRecordJpaRepository disorderRecordJpaRepository;

    @Override
    public DisorderRecord save(DisorderRecord disorderRecord) {
        return disorderRecordJpaRepository.save(disorderRecord);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public DisorderRecord findById(Long id) {
        return disorderRecordJpaRepository.findById(id).orElse(null);
    }

    @Transactional
    @FilterSoftDelete
    @Override
    public CursorPage<DisorderRecord> findByPetId(Long petId, Long cursor, int size) {

        String q = "select r.id from DisorderRecord r where (:cursor is null or r.dateTime < :cursor) and r.petId = :petId order by r.dateTime desc";
        List<Long> ids = entityManager.createQuery(q, Long.class)
                .setParameter("petId", petId)
                .setParameter("cursor", cursor != null ? Instant.ofEpochMilli(cursor) : null)
                .setMaxResults(size).getResultList();

        if (ids.size() == 0) {
            return CursorPage.empty(size);
        }

        Long nextCursor = ids.get(ids.size() - 1);
        String cntq = "select count(r) from DisorderRecord r where (:cursor is null or r.dateTime < :cursor) and r.petId = :petId order by r.dateTime desc";
        Long cnt =  entityManager.createQuery(cntq, Long.class)
                .setParameter("petId", petId)
                .setParameter("cursor", Instant.ofEpochMilli(nextCursor))
                .getSingleResult();

        List<DisorderRecord> records = disorderRecordJpaRepository.findByIdInOrderByIdDesc(ids);

        return CursorPage.of(records, cnt > 0, size);
    }

    @Override
    public void remove(DisorderRecord disorderRecord) {
        disorderRecord.markDelete();
        disorderRecordJpaRepository.save(disorderRecord);
    }
}
