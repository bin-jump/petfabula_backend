package com.petfabula.infrastructure.persistence.jpa.pet.impl;

import com.petfabula.domain.aggregate.pet.entity.FeedRecord;
import com.petfabula.domain.aggregate.pet.respository.FeedRecordRepository;
import com.petfabula.domain.common.paging.CursorPage;
import com.petfabula.infrastructure.persistence.jpa.pet.repository.FeedRecordJpaRepository;
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

@Repository
public class FeedRecordRepositoryImpl implements FeedRecordRepository {

    @Autowired
    private FeedRecordJpaRepository feedRecordJpaRepository;

    @Override
    public FeedRecord save(FeedRecord disorderRecord) {
        return feedRecordJpaRepository.save(disorderRecord);
    }

    @Override
    public FeedRecord findById(Long id) {
        return feedRecordJpaRepository.findById(id).orElse(null);
    }

    @Override
    public CursorPage<FeedRecord> findByPetId(Long petId, Long cursor, int size) {
        Specification<FeedRecord> spec = new Specification<FeedRecord>() {
            @Override
            public Predicate toPredicate(Root<FeedRecord> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                cq.orderBy(cb.asc(root.get("id")));
                Predicate aPred = cb.equal(root.get("petId"), petId);
                if (cursor != null) {
                    Predicate cPred = cb.greaterThan(root.get("id"), cursor);
                    return cb.and(aPred, cPred);
                }
                return aPred;
            }
        };

        Pageable limit = PageRequest.of(0, size);
        Page<FeedRecord> res = feedRecordJpaRepository.findAll(spec, limit);
        return CursorPage.of(res.getContent(), res.hasNext(), size);
    }

    @Override
    public void remove(FeedRecord disorderRecord) {
        feedRecordJpaRepository.delete(disorderRecord);
    }
}
